package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class DaySchedule(
    @SerializedName("id")
    val id: Int, // inner day id
    @SerializedName("date")
    val date: Int, // UNIX Timestamp since 1970 00:00
    @SerializedName("available_periods")
    val availablePeriods: List<String>, // list of available period IDs
    @SerializedName("records")
    val records: List<Record> // list of associated records
)
