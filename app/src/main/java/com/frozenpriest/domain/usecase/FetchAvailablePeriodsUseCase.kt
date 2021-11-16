package com.frozenpriest.domain.usecase

import com.frozenpriest.data.remote.DoctorScheduleApi
import com.frozenpriest.data.remote.response.AvailablePeriod
import timber.log.Timber
import javax.inject.Inject

interface FetchAvailablePeriodsUseCase {
    suspend operator fun invoke(): Result<List<AvailablePeriod>>
}

class FetchAvailablePeriodsUseCaseImpl @Inject constructor(
    private val api: DoctorScheduleApi
) : FetchAvailablePeriodsUseCase {
    override suspend fun invoke(): Result<List<AvailablePeriod>> {
        return try {
            val response = api.getAvailablePeriods()
            Timber.e("Available" + response.availablePeriods.toString())
            Result.success(response.availablePeriods)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
