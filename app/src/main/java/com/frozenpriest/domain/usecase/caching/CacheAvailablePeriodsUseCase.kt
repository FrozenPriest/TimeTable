package com.frozenpriest.domain.usecase.caching

import com.frozenpriest.data.local.converters.toEntity
import com.frozenpriest.data.local.dao.RecordsDao
import com.frozenpriest.data.remote.response.AvailablePeriod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface CacheAvailablePeriodsUseCase {
    suspend operator fun invoke(availablePeriods: List<AvailablePeriod>)
}

class CacheAvailablePeriodsUseCaseImpl @Inject constructor(
    private val dao: RecordsDao
) : CacheAvailablePeriodsUseCase {
    override suspend fun invoke(availablePeriods: List<AvailablePeriod>) = withContext(Dispatchers.IO) {
        try {
            dao.insertPeriods(availablePeriods.map { it.toEntity() })
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
