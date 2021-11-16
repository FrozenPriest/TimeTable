package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class DayScheduleResponse(
    @SerializedName("results")
    val schedules: List<DaySchedule> // list of associated record ids
)
