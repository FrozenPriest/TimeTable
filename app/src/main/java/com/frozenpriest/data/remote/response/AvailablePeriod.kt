package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class AvailablePeriod(
    @SerializedName("objectId")
    val id: String, // some period id
    @SerializedName("start")
    val start: Int, // Start time in seconds after 00:00
    @SerializedName("end")
    val end: Int // End time in seconds after 00:00
)
