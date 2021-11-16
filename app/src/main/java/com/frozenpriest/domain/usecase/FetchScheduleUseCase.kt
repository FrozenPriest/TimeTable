package com.frozenpriest.domain.usecase

import android.graphics.Color
import com.frozenpriest.data.local.LocalDaySchedule
import com.frozenpriest.data.local.LocalDoctorSchedule
import com.frozenpriest.data.local.Record
import com.frozenpriest.data.local.RecordType
import com.frozenpriest.data.remote.DoctorScheduleApi
import com.frozenpriest.data.remote.IsoDateFormatter
import com.frozenpriest.data.remote.response.AvailablePeriod
import com.frozenpriest.data.remote.response.AvailableStatus
import com.frozenpriest.data.remote.response.AvailableType
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
                "{ \"doctor\":\"$doctorId\", \"date\":{\"\$gte\": {\"__type\":\"Date\",\"iso\":\"${startDate}\"}, \"\$lte\": {\"__type\":\"Date\",\"iso\":\"${endDate}\"}} }"
            )
            if (daySchedules.schedules.isEmpty()) return Result.success(
                LocalDoctorSchedule(
                    doctorInfo.id, doctorInfo.name, doctorInfo.organization, emptyMap()
                )
            )

            val recordIds = mutableListOf<String>()
            daySchedules.schedules.forEach { recordIds.addAll(it.records) }
            // getRecords() with record id
            val records = doctorScheduleApi.getRecords(
                "{ \"objectId\":{\"\$in\": [${
                recordIds.joinToString(
                    "\", \"",
                    "\"",
                    "\""
                )
                }]} }"
            )

            // getPatients() with patient ids
            val patientIds = records.records.map { it.patient }
            val patients = doctorScheduleApi.getPatients(
                "{ \"objectId\":{\"\$in\": [${
                patientIds.joinToString(
                    "\", \"",
                    "\"",
                    "\""
                )
                }]} }"
            )

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
                LocalDaySchedule(
                    id = currentDaySchedule.id,
                    date = isoDateFormatter.stringToDate(currentDaySchedule.date.date),
                    records = recs.sortedBy { it.start }
                )
            }
            return Result.success(
                LocalDoctorSchedule(
                    doctorId = doctorInfo.id,
                    name = doctorInfo.name,
                    organization = doctorInfo.organization,
                    daySchedules = newDaySchedules.associateBy { it.date }
                )
            )
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure(e)
        }
    }
}
