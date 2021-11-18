package com.frozenpriest.domain.usecase.caching

import com.frozenpriest.data.local.converters.toEntity
import com.frozenpriest.data.local.dao.RecordsDao
import com.frozenpriest.data.remote.response.AvailableType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface CacheAvailableTypesUseCase {
    suspend operator fun invoke(types: List<AvailableType>)
}

class CacheAvailableTypesUseCaseImpl @Inject constructor(
    private val dao: RecordsDao
) : CacheAvailableTypesUseCase {
    override suspend fun invoke(types: List<AvailableType>) = withContext(Dispatchers.IO) {
        try {
            dao.insertTypes(types.map { it.toEntity() })
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
