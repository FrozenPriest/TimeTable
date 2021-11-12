package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class DoctorSchedule(
    @SerializedName("id")
    val id: Int, // doctor id
    @SerializedName("name")
    val name: String, // doctor name
    @SerializedName("organization")
    val organization: Organization, // doctor organization
    @SerializedName("days")
    val daySchedules: List<DaySchedule> // list of day schedules
)
