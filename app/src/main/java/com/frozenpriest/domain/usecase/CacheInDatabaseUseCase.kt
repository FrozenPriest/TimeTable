package com.frozenpriest.domain.usecase

import com.frozenpriest.data.local.converters.toEntity
import com.frozenpriest.data.local.dao.RecordsDao
import com.frozenpriest.data.remote.response.AvailablePeriod
import com.frozenpriest.data.remote.response.AvailableStatus
import com.frozenpriest.data.remote.response.AvailableType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface CacheInDatabaseUseCase {
    suspend operator fun invoke(
        periods: List<AvailablePeriod>,
        types: List<AvailableType>,
        statuses: List<AvailableStatus>
    )
}

class CacheInDatabaseUseCaseImpl @Inject constructor(
    private val dao: RecordsDao
) : CacheInDatabaseUseCase {
    override suspend fun invoke(
        periods: List<AvailablePeriod>,
        types: List<AvailableType>,
        statuses: List<AvailableStatus>
    ) = withContext(Dispatchers.IO) {
        try {
            dao.insertPeriods(periods.map { it.toEntity() })
            dao.insertStatuses(statuses.map { it.toEntity() })
            dao.insertTypes(types.map { it.toEntity() })
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
