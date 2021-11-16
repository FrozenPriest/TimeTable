package com.frozenpriest.data.remote

import com.frozenpriest.data.remote.response.AvailablePeriodsResponse
import com.frozenpriest.data.remote.response.AvailableStatusesResponse
import com.frozenpriest.data.remote.response.AvailableTypesResponse
import com.frozenpriest.data.remote.response.DayScheduleResponse
import com.frozenpriest.data.remote.response.PatientsResponse
import com.frozenpriest.data.remote.response.RecordsResponse
import com.frozenpriest.data.remote.response.ScheduleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DoctorScheduleApi {
    @GET("classes/doctor_schedules")
    suspend fun getDoctor(
        @Query("where", encoded = true) params: String
    ): ScheduleResponse

    @GET("classes/available_periods")
    suspend fun getAvailablePeriods(): AvailablePeriodsResponse

    @GET("classes/available_statuses")
    suspend fun getAvailableStatuses(): AvailableStatusesResponse

    @GET("classes/available_types")
    suspend fun getAvailableTypes(): AvailableTypesResponse

    @GET("classes/day_schedules")
    suspend fun getDaySchedules(@Query("where", encoded = true) params: String): DayScheduleResponse

    @GET("classes/records")
    suspend fun getRecords(@Query("where", encoded = true) params: String): RecordsResponse

    @GET("classes/patients")
    suspend fun getPatients(@Query("where", encoded = true) params: String): PatientsResponse
}
