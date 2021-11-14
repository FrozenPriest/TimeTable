package com.frozenpriest.domain.usecase

import com.frozenpriest.data.remote.DoctorScheduleApi
import com.frozenpriest.data.remote.response.DaySchedule
import com.frozenpriest.data.remote.response.DoctorSchedule
import com.frozenpriest.data.remote.response.Organization

@Suppress("MagicNumber", "UnusedPrivateMember")
class DummyFetchQuestionUseCase(private val doctorScheduleApi: DoctorScheduleApi) : FetchScheduleUseCase {
    override suspend fun invoke(
        doctorId: Int,
        week: Int,
        month: Int,
        year: Int
    ): Result<DoctorSchedule> {
        return Result.success(
            DoctorSchedule(
                1,
                "Dr. Stone",
                Organization("Stone Inc."),
                listOf(DaySchedule(1, 12, emptyList(), emptyList()))
            )
        )
    }
}
