package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("objectId")
    val id: String, // record id
    @SerializedName("status")
    val status: String, // available status id
    @SerializedName("types")
    val types: List<String>, // available type ids
    @SerializedName("date")
    val date: Int, // UNIX Timestamp since 1970 00:00
    @SerializedName("patient")
    val patient: String, // associated patient id
    @SerializedName("reason")
    val reason: String, // record's reason
    @SerializedName("room")
    val room: String, // associated room
    @SerializedName("start")
    val start: Int, // Start time in seconds after 00:00
    @SerializedName("end")
    val end: Int // End time in seconds after 00:00

)
