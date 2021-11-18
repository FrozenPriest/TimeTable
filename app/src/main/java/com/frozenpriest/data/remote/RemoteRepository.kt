package com.frozenpriest.data.remote

import com.frozenpriest.data.remote.response.AvailablePeriod
import com.frozenpriest.data.remote.response.AvailableStatus
import com.frozenpriest.data.remote.response.AvailableType
import javax.inject.Inject

interface RemoteRepository {
    suspend fun getAvailablePeriods(): Result<List<AvailablePeriod>>
    suspend fun getAvailableStatuses(): Result<List<AvailableStatus>>
    suspend fun getAvailableTypes(): Result<List<AvailableType>>
}

class RemoteRepositoryImpl @Inject constructor(
    private val api: DoctorScheduleApi
) : RemoteRepository {
    override suspend fun getAvailablePeriods(): Result<List<AvailablePeriod>> {
        return try {
            val response = api.getAvailablePeriods()
            Result.success(response.availablePeriods)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAvailableStatuses(): Result<List<AvailableStatus>> {
        return try {
            val response = api.getAvailableStatuses()
            Result.success(response.availableStatuses)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAvailableTypes(): Result<List<AvailableType>> {
        return try {
            val response = api.getAvailableTypes()
            Result.success(response.availableTypes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
