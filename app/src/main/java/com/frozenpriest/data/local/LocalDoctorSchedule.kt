package com.frozenpriest.data.local

import java.util.*

data class LocalDoctorSchedule(
    val doctorId: String,
    val name: String,
    val organization: String,
    val daySchedules: Map<Date, LocalDaySchedule>
)
