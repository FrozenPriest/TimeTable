package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

// api/demo/schedule?id=13243&month=10&year=2020, where id is ID of doctor
data class ScheduleResponse(
    @SerializedName("schedule")
    val schedule: DoctorSchedule
)
