package com.frozenpriest.domain

import android.graphics.Color
import com.frozenpriest.data.remote.response.AvailablePeriod
import com.frozenpriest.data.remote.response.AvailableStatus
import com.frozenpriest.data.remote.response.AvailableType
import com.frozenpriest.data.remote.response.PatientsResponse
import com.frozenpriest.data.remote.response.RecordsResponse
import com.frozenpriest.domain.model.Record
import com.frozenpriest.domain.model.RecordType

class RecordCreator {
    fun makeOccupiedRecords(
        records: RecordsResponse,
        availableStatuses: List<AvailableStatus>,
        availableTypes: List<AvailableType>,
        patients: PatientsResponse
    ): List<Record> {
        return records.records.map { record ->
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
    }

    fun makeEmptySlotRecords(
        availablePeriods: List<AvailablePeriod>,
        currentDayAvailablePeriods: List<String>
    ): List<Record> {
        return availablePeriods.filter {
            currentDayAvailablePeriods.contains(it.id)
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
    }

    fun makeNoShiftRecords(
        availablePeriods: List<AvailablePeriod>,
        currentDayAvailablePeriods: List<String>
    ): List<Record> {
        val result = mutableListOf<Record>()
        var cumulative = -1
        for (i in availablePeriods.sortedBy { it.start }.indices) {
            if (!currentDayAvailablePeriods.contains(availablePeriods[i].id)) {
                when {
                    cumulative == -1 -> {
                        cumulative = i
                    }
                    availablePeriods[i - 1].end != availablePeriods[i].start -> {
                        // add prev group
                        result.add(
                            makeNoShiftRecord(availablePeriods, cumulative, i - 1)
                        )
                        cumulative = i
                    }
                    i == availablePeriods.lastIndex -> {
                        result.add(
                            makeNoShiftRecord(availablePeriods, cumulative, i)
                        )
                    }
                }
            } else if (cumulative != -1) {
                result.add(
                    makeNoShiftRecord(availablePeriods, cumulative, i - 1)
                )
                cumulative = -1
            }
        }
        return result
    }

    private fun makeNoShiftRecord(
        availablePeriods: List<AvailablePeriod>,
        cumulative: Int,
        i: Int
    ) = Record(
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
}
