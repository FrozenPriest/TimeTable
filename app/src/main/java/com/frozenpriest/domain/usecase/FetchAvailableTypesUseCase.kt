package com.frozenpriest.domain.usecase

import com.frozenpriest.data.remote.DoctorScheduleApi
import com.frozenpriest.data.remote.response.AvailableType
import javax.inject.Inject

interface FetchAvailableTypesUseCase {
    suspend operator fun invoke(): Result<List<AvailableType>>
}

class FetchAvailableTypesUseCaseImpl @Inject constructor(
    private val api: DoctorScheduleApi
) : FetchAvailableTypesUseCase {
    override suspend fun invoke(): Result<List<AvailableType>> {
        return try {
            val response = api.getAvailableTypes()
            Result.success(response.availableTypes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
