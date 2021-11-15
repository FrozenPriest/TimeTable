package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class AvailableStatus(
    @SerializedName("objectId")
    val id: String, // status id
    @SerializedName("name")
    val name: String // status name
)
