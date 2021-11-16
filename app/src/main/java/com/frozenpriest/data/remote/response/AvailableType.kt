package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class AvailableType(
    @SerializedName("objectId")
    val id: String, // type id
    @SerializedName("name")
    val name: String // type name
)
