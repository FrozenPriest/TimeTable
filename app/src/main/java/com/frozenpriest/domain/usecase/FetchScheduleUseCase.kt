@file:Suppress("LongParameterList")

package com.frozenpriest.domain.usecase

import com.frozenpriest.data.remote.DoctorScheduleApi
import com.frozenpriest.data.remote.response.AvailablePeriod
import com.frozenpriest.data.remote.response.AvailableStatus
import com.frozenpriest.data.remote.response.AvailableType
import com.frozenpriest.data.remote.response.DayScheduleResponse
import com.frozenpriest.data.remote.response.PatientsResponse
import com.frozenpriest.data.remote.response.RecordsResponse
import com.frozenpriest.domain.RecordCreator
import com.frozenpriest.domain.model.LocalDaySchedule
import com.frozenpriest.domain.model.LocalDoctorSchedule
import com.frozenpriest.domain.model.Record
import com.frozenpriest.utils.IsoDateFormatter
import timber.log.Timber
import javax.inject.Inject

interface FetchScheduleUseCase {
    suspend operator fun invoke(
        doctorId: String,
        week: Int,
        month: Int,
        year: Int,
        availablePeriods: List<AvailablePeriod>,
        availableStatuses: List<AvailableStatus>,
        availableTypes: List<AvailableType>
    ): Result<LocalDoctorSchedule>
}

// следует отрефакторить
class FetchScheduleUseCaseImpl @Inject constructor(
    private val doctorScheduleApi: DoctorScheduleApi,
    private val recordsCreator: RecordCreator
) : FetchScheduleUseCase {
    override suspend operator fun invoke(
        doctorId: String,
        week: Int,
        month: Int,
        year: Int,
        availablePeriods: List<AvailablePeriod>,
        availableStatuses: List<AvailableStatus>,
        availableTypes: List<AvailableType>
    ): Result<LocalDoctorSchedule> {
        return try {
            val isoDateFormatter = IsoDateFormatter()
            // getDoctorInfo() (doctor_schedules)
            val doctorInfo =
                doctorScheduleApi.getDoctor("{ \"objectId\":\"$doctorId\" }").schedule.first()

            // getDaySchedules() with doctor id and start-finish dates
            val (startDate, endDate) = isoDateFormatter.getWeekStartEndDates(week, month, year)

            val daySchedules = doctorScheduleApi.getDaySchedules(
                formatDoctorScheduleRequest(doctorId, startDate, endDate)
            )
            if (daySchedules.schedules.isEmpty()) Result.success(
                LocalDoctorSchedule(
                    doctorInfo.id, doctorInfo.name, doctorInfo.organization, emptyMap()
                )
            ) else {

                val records = getRecordsFromDaySchedules(daySchedules)

                // getPatients() with patient ids
                val patients = getPatientsFromRecordsIds(records)

                val newDaySchedules = buildDaySchedules(
                    daySchedules,
                    records,
                    availablePeriods,
                    availableStatuses,
                    availableTypes,
                    patients,
                )
                Result.success(
                    LocalDoctorSchedule(
                        doctorId = doctorInfo.id,
                        name = doctorInfo.name,
                        organization = doctorInfo.organization,
                        daySchedules = newDaySchedules.associateBy { it.date }
                    )
                )
            }
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure(e)
        }
    }

    private fun formatDoctorScheduleRequest(
        doctorId: String,
        startDate: String,
        endDate: String
    ): String {
        return "{ \"doctor\":\"$doctorId\",\"date\":{ \"\$gte\":" +
            "{ \"__type\":\"Date\",\"iso\":\"$startDate\" }," +
            "\"\$lte\":{ \"__type\":\"Date\",\"iso\":\"$endDate\" } } }"
    }

    private suspend fun getRecordsFromDaySchedules(daySchedules: DayScheduleResponse): RecordsResponse {
        val recordIds = mutableListOf<String>()
        daySchedules.schedules.forEach { recordIds.addAll(it.records) }
        // getRecords() with record id
        return doctorScheduleApi.getRecords(
            "{ \"objectId\":{\"\$in\": [${
            recordIds.joinToString(
                "\", \"",
                "\"",
                "\""
            )
            }]} }"
        )
    }

    private suspend fun getPatientsFromRecordsIds(records: RecordsResponse): PatientsResponse {
        val patientIds = records.records.map { it.patient }
        return doctorScheduleApi.getPatients(
            "{ \"objectId\":{\"\$in\": [${
            patientIds.joinToString(
                "\", \"",
                "\"",
                "\""
            )
            }]} }"
        )
    }

    private fun buildDaySchedules(
        daySchedulesResponse: DayScheduleResponse,
        records: RecordsResponse,
        availablePeriods: List<AvailablePeriod>,
        availableStatuses: List<AvailableStatus>,
        availableTypes: List<AvailableType>,
        patients: PatientsResponse,
    ): List<LocalDaySchedule> {
        val isoDateFormatter = IsoDateFormatter()

        val newDaySchedules = daySchedulesResponse.schedules.map { currentDaySchedule ->
            val recs = mutableListOf<Record>()
            recs.addAll(
                recordsCreator.makeEmptySlotRecords(
                    availablePeriods,
                    currentDaySchedule.availablePeriods
                )
            )
            recs.addAll(
                recordsCreator.makeOccupiedRecords(
                    records,
                    availableStatuses,
                    availableTypes,
                    patients
                )
            )
            recs.addAll(
                recordsCreator.makeNoShiftRecords(
                    availablePeriods,
                    currentDaySchedule.availablePeriods
                )
            )

            LocalDaySchedule(
                id = currentDaySchedule.id,
                date = isoDateFormatter.stringToDate(currentDaySchedule.date.date),
                records = recs.sortedBy { it.start },
                availablePeriods = availablePeriods.filter {
                    currentDaySchedule.availablePeriods.contains(
                        it.id
                    )
                }
            )
        }
        return newDaySchedules
    }
}
