package com.frozenpriest.domain.model

import java.util.Date

data class LocalDoctorSchedule(
    val doctorId: String,
    val name: String,
    val organization: String,
    val daySchedules: Map<Date, LocalDaySchedule>
)
