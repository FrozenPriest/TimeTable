package com.frozenpriest.domain.usecase

import com.frozenpriest.data.local.converters.fromEntity
import com.frozenpriest.data.local.converters.toRecord
import com.frozenpriest.data.local.dao.RecordsDao
import com.frozenpriest.domain.RecordCreator
import com.frozenpriest.domain.model.LocalDaySchedule
import com.frozenpriest.domain.model.LocalDoctorSchedule
import com.frozenpriest.domain.model.Record
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

interface LoadCachedUseCase {
    suspend operator fun invoke(
        doctorId: String,
        start: Date,
        end: Date
    ): LocalDoctorSchedule?
}

class LoadCachedUseCaseImpl @Inject constructor(
    private val dao: RecordsDao,
    private val recordsCreator: RecordCreator
) : LoadCachedUseCase {
    override suspend fun invoke(
        doctorId: String,
        start: Date,
        end: Date
    ): LocalDoctorSchedule? {
        return withContext(Dispatchers.IO) {
            try {
                val periods = dao.getPeriods().map { it.fromEntity() }

                val doctorWithRecords = dao.getDoctorWithRecordsInPeriod(
                    doctorId,
                    start.time,
                    end.time
                ) ?: return@withContext null

                val days = dao.getDaysInPeriod(
                    doctorWithRecords.doctor.id,
                    start.time,
                    end.time
                )

                val newDays = days.map { dayEntity ->
                    val records = mutableListOf<Record>()

                    records.addAll(
                        recordsCreator.makeEmptySlotRecords(
                            periods,
                            dayEntity.availablePeriods
                        )
                    )
                    records.addAll(
                        doctorWithRecords.records
                            .filter { it.recordEntity.dayId == dayEntity.id }
                            .map { it.toRecord() }
                    )
                    records.addAll(
                        recordsCreator.makeNoShiftRecords(
                            periods,
                            dayEntity.availablePeriods
                        )
                    )

                    LocalDaySchedule(
                        id = dayEntity.id,
                        date = Date(dayEntity.date),
                        availablePeriods = periods.filter { dayEntity.availablePeriods.contains(it.id) },
                        records = records.sortedBy { it.start }

                    )
                }.groupBy { it.date }.mapValues { it.value.first() }

                return@withContext LocalDoctorSchedule(
                    doctorId = doctorId,
                    name = doctorWithRecords.doctor.name,
                    organization = doctorWithRecords.doctor.organization,
                    daySchedules = newDays
                )
            } catch (e: Exception) {
                Timber.e(e)
                return@withContext null
            }
        }
    }
}
