package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("id")
    val id: Int, // record id
    @SerializedName("status")
    val status: String, // available status id
    @SerializedName("types")
    val types: List<String>, // available type ids
    @SerializedName("date")
    val date: Int, // UNIX Timestamp since 1970 00:00
    @SerializedName("pacient")
    val patient: Patient, // associated patient
    @SerializedName("reason")
    val reason: String, // record's reason
    @SerializedName("room")
    val room: Room, // associated room
    @SerializedName("start")
    val start: Int, // Start time in seconds after 00:00
    @SerializedName("end")
    val end: Int // End time in seconds after 00:00

)
