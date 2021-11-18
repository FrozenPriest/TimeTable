package com.frozenpriest.domain.usecase.caching

import com.frozenpriest.data.local.converters.toEntity
import com.frozenpriest.data.local.dao.RecordsDao
import com.frozenpriest.data.remote.response.AvailableStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface CacheAvailableStatusesUseCase {
    suspend operator fun invoke(statuses: List<AvailableStatus>)
}

class CacheAvailableStatusesUseCaseImpl @Inject constructor(
    private val dao: RecordsDao
) : CacheAvailableStatusesUseCase {
    override suspend fun invoke(statuses: List<AvailableStatus>) = withContext(Dispatchers.IO) {
        try {
            dao.insertStatuses(statuses.map { it.toEntity() })
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
