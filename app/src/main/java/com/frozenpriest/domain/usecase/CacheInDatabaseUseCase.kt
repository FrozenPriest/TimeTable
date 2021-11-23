package com.frozenpriest.domain.usecase

import com.frozenpriest.data.local.converters.toEntity
import com.frozenpriest.data.local.converters.toFullRecord
import com.frozenpriest.data.local.dao.RecordsDao
import com.frozenpriest.data.local.entities.DayEntity
import com.frozenpriest.data.local.entities.DoctorEntity
import com.frozenpriest.data.local.entities.query.FullRecord
import com.frozenpriest.data.remote.response.AvailablePeriod
import com.frozenpriest.data.remote.response.AvailableStatus
import com.frozenpriest.data.remote.response.AvailableType
import com.frozenpriest.domain.model.LocalDoctorSchedule
import com.frozenpriest.domain.model.RecordType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface CacheInDatabaseUseCase {
    suspend operator fun invoke(
        periods: List<AvailablePeriod>,
        types: List<AvailableType>,
        statuses: List<AvailableStatus>,
        schedule: LocalDoctorSchedule
    )
}

class CacheInDatabaseUseCaseImpl @Inject constructor(
    private val dao: RecordsDao
) : CacheInDatabaseUseCase {
    override suspend fun invoke(
        periods: List<AvailablePeriod>,
        types: List<AvailableType>,
        statuses: List<AvailableStatus>,
        schedule: LocalDoctorSchedule
    ) = withContext(Dispatchers.IO) {
        try {
            dao.insertPeriods(periods.map { it.toEntity() })
            dao.insertStatuses(statuses.map { it.toEntity() })
            dao.insertTypes(types.map { it.toEntity() })

            dao.insertDoctor(
                DoctorEntity(
                    id = schedule.doctorId,
                    name = schedule.name,
                    organization = schedule.organization
                )
            )

            dao.insertDaySchedules(
                schedule.daySchedules.values.map { localDaySchedule ->
                    DayEntity(
                        doctorId = schedule.doctorId,
                        id = localDaySchedule.id,
                        date = localDaySchedule.date.time,
                        availablePeriods = localDaySchedule.availablePeriods.map { it.id }
                    )
                }
            )

            val records = mutableListOf<FullRecord>()
            schedule.daySchedules.forEach { entry ->
                records.addAll(
                    entry.value.records
                        .filter { it.recordType == RecordType.OCCUPIED }
                        .map { it.toFullRecord(entry.key.time, entry.value.id) }
                )
            }
            dao.insertFullRecords(records)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
