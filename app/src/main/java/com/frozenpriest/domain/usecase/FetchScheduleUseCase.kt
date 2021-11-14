package com.frozenpriest.domain.usecase

import com.frozenpriest.data.remote.DoctorScheduleApi
import com.frozenpriest.data.remote.response.DoctorSchedule
import javax.inject.Inject

interface FetchScheduleUseCase {
    suspend operator fun invoke(
        doctorId: Int,
        week: Int,
        month: Int,
        year: Int
    ): Result<DoctorSchedule>
}

class FetchScheduleUseCaseImpl @Inject constructor(
    private val doctorScheduleApi: DoctorScheduleApi
) : FetchScheduleUseCase {
    override suspend operator fun invoke(
        doctorId: Int,
        week: Int,
        month: Int,
        year: Int
    ): Result<DoctorSchedule> {
        return try {
            val result = doctorScheduleApi.getDoctorSchedule(doctorId, week, month, year)
            Result.success(result.schedule)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
