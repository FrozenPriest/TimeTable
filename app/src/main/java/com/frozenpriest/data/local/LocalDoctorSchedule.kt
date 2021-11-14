package com.frozenpriest.data.local

import com.frozenpriest.data.remote.response.Organization

data class LocalDoctorSchedule(
    val doctorId: Int,
    val name: String,
    val organization: Organization,
    val daySchedules: List<LocalDaySchedule>
)
