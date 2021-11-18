@file:Suppress("LongParameterList")

package com.frozenpriest.domain.usecase

import android.graphics.Color
import com.frozenpriest.data.remote.DoctorScheduleApi
import com.frozenpriest.data.remote.response.AvailablePeriod
import com.frozenpriest.data.remote.response.AvailableStatus
import com.frozenpriest.data.remote.response.AvailableType
import com.frozenpriest.data.remote.response.DayScheduleResponse
import com.frozenpriest.data.remote.response.PatientsResponse
import com.frozenpriest.data.remote.response.RecordsResponse
import com.frozenpriest.domain.model.LocalDaySchedule
import com.frozenpriest.domain.model.LocalDoctorSchedule
import com.frozenpriest.domain.model.Record
import com.frozenpriest.domain.model.RecordType
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

class FetchScheduleUseCaseImpl @Inject constructor(
    private val doctorScheduleApi: DoctorScheduleApi
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
            val (startDate, endDate) = isoDateFormatter.getWeekStartEndDates()

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
        daySchedules: DayScheduleResponse,
        records: RecordsResponse,
        availablePeriods: List<AvailablePeriod>,
        availableStatuses: List<AvailableStatus>,
        availableTypes: List<AvailableType>,
        patients: PatientsResponse,
    ): List<LocalDaySchedule> {
        val isoDateFormatter = IsoDateFormatter()

        val newDaySchedules = daySchedules.schedules.map { currentDaySchedule ->
            val recs = mutableListOf<Record>()
            recs.addAll(
                availablePeriods.filter {
                    currentDaySchedule.availablePeriods.contains(it.id)
                }.map {
                    Record(
                        start = it.start,
                        end = it.end,
                        recordType = RecordType.EMPTY,
                        id = null,
                        status = null,
                        types = null,
                        patient = null,
                        reason = null,
                        room = null,
                        backgroundColor = null,
                        dividerColor = null
                    )
                }
            )
            recs.addAll(
                records.records.map { record ->
                    Record(
                        id = record.id,
                        status = availableStatuses.find { it.id == record.status },
                        types = availableTypes.filter { record.types.contains(it.id) },
                        patient = patients.records.first { it.id == record.patient },
                        reason = record.reason,
                        room = record.room,
                        start = record.start,
                        end = record.end,
                        recordType = RecordType.OCCUPIED,
                        backgroundColor = Color.LTGRAY,
                        dividerColor = Color.MAGENTA
                    )
                }
            )
            // берем все периоды, которые не в списке. Крайние сливаем в один
            var cumulative = -1
            for (i in availablePeriods.sortedBy { it.start }.indices) {
                if (!currentDaySchedule.availablePeriods.contains(availablePeriods[i].id)) {
                    when {
                        cumulative == -1 -> {
                            cumulative = i
                        }
                        availablePeriods[i - 1].end != availablePeriods[i].start -> {
                            // add prev group
                            recs.add(
                                Record(
                                    start = availablePeriods[cumulative].start,
                                    end = availablePeriods[i - 1].end,
                                    recordType = RecordType.NO_SHIFT,
                                    id = null,
                                    status = null,
                                    types = null,
                                    patient = null,
                                    reason = null,
                                    room = null,
                                    backgroundColor = null,
                                    dividerColor = null
                                )
                            )
                            cumulative = i
                        }
                        i == availablePeriods.lastIndex -> {
                            recs.add(
                                Record(
                                    start = availablePeriods[cumulative].start,
                                    end = availablePeriods[i].end,
                                    recordType = RecordType.NO_SHIFT,
                                    id = null,
                                    status = null,
                                    types = null,
                                    patient = null,
                                    reason = null,
                                    room = null,
                                    backgroundColor = null,
                                    dividerColor = null
                                )
                            )
                        }
                    }
                } else if (cumulative != -1) {
                    recs.add(
                        Record(
                            start = availablePeriods[cumulative].start,
                            end = availablePeriods[i - 1].end,
                            recordType = RecordType.NO_SHIFT,
                            id = null,
                            status = null,
                            types = null,
                            patient = null,
                            reason = null,
                            room = null,
                            backgroundColor = null,
                            dividerColor = null
                        )
                    )
                    cumulative = -1
                }
            }
            LocalDaySchedule(
                id = currentDaySchedule.id,
                date = isoDateFormatter.stringToDate(currentDaySchedule.date.date),
                records = recs.sortedBy { it.start }
            )
        }
        return newDaySchedules
    }
}
