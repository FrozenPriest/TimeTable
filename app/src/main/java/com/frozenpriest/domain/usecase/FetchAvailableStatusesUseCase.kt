package com.frozenpriest.domain.usecase

import com.frozenpriest.data.remote.DoctorScheduleApi
import com.frozenpriest.data.remote.response.AvailableStatus
import javax.inject.Inject

interface FetchAvailableStatusesUseCase {
    suspend operator fun invoke(): Result<List<AvailableStatus>>
}

class FetchAvailableStatusesUseCaseImpl @Inject constructor(
    private val api: DoctorScheduleApi
) : FetchAvailableStatusesUseCase {
    override suspend fun invoke(): Result<List<AvailableStatus>> {
        return try {
            val response = api.getAvailableStatuses()
            Result.success(response.availableStatuses)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
