package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class DaySchedule(
    @SerializedName("objectId")
    val id: String, // inner day id
    @SerializedName("date")
    val date: IsoDate, // UNIX Timestamp since 1970 00:00
    @SerializedName("available_periods")
    val availablePeriods: List<String>, // list of available period IDs
    @SerializedName("records")
    val records: List<String> // list of associated record ids
)

data class IsoDate(
    @SerializedName("iso")
    val date: String
)
