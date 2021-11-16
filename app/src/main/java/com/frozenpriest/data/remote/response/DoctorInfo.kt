package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class DoctorInfo(
    @SerializedName("objectId")
    val id: String, // doctor id
    @SerializedName("name")
    val name: String, // doctor name
    @SerializedName("organization")
    val organization: String, // doctor organization
)
