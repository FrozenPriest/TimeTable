package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class AvailableType(
    @SerializedName("id")
    val id: Int, // type id
    @SerializedName("name")
    val name: String // type name
)
