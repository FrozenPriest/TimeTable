package com.frozenpriest.data.remote

import com.frozenpriest.data.remote.response.AvailablePeriodsResponse
import com.frozenpriest.data.remote.response.AvailableStatusesResponse
import com.frozenpriest.data.remote.response.AvailableTypesResponse
import com.frozenpriest.data.remote.response.ScheduleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DoctorScheduleApi {
    @GET("api/demo/schedule")
    suspend fun getDoctorSchedule(
        @Query("id") id: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): ScheduleResponse

    @GET("api/demo/periods")
    suspend fun getAvailablePeriods(): AvailablePeriodsResponse

    @GET("api/demo/statuses")
    suspend fun getAvailableStatuses(): AvailableStatusesResponse

    @GET("api/demo/types")
    suspend fun getAvailableTypes(): AvailableTypesResponse
}
