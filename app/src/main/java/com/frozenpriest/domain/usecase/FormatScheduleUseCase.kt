package com.frozenpriest.domain.usecase

import android.graphics.Color
import com.frozenpriest.data.local.LocalDaySchedule
import com.frozenpriest.data.local.LocalDoctorSchedule
import com.frozenpriest.data.local.Record
import com.frozenpriest.data.local.RecordType
import com.frozenpriest.data.remote.response.AvailablePeriod
import com.frozenpriest.data.remote.response.AvailableStatus
import com.frozenpriest.data.remote.response.AvailableType
import com.frozenpriest.data.remote.response.DoctorSchedule

interface FormatScheduleUseCase {
    operator fun invoke(
        doctorSchedule: DoctorSchedule,
        availablePeriods: List<AvailablePeriod>,
        availableStatuses: List<AvailableStatus>,
        availableTypes: List<AvailableType>
    ): LocalDoctorSchedule
}

class FormatScheduleUseCaseImpl : FormatScheduleUseCase {
    override operator fun invoke(
        doctorSchedule: DoctorSchedule,
        availablePeriods: List<AvailablePeriod>,
        availableStatuses: List<AvailableStatus>,
        availableTypes: List<AvailableType>
    ): LocalDoctorSchedule {
        val daySchedules = doctorSchedule.daySchedules.map { currentDaySchedule ->
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
                currentDaySchedule.records.map { record ->
                    Record(
                        id = record.id,
                        status = availableStatuses.find { it.id == record.status },
                        types = availableTypes.filter { record.types.contains(it.id) },
                        patient = record.patient,
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
                date = currentDaySchedule.date,
                records = recs.sortedBy { it.start }
            )
        }
        return LocalDoctorSchedule(
            doctorId = doctorSchedule.id,
            name = doctorSchedule.name,
            organization = doctorSchedule.organization,
            daySchedules = daySchedules
        )
    }
}
