package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class Patient(
    @SerializedName("id")
    val id: Int, // patient inner id
    @SerializedName("med_id")
    val medId: String, // patient medicine id
    @SerializedName("name")
    val name: String, // patient name
    @SerializedName("phone")
    val phone: String // patient phone number
)
