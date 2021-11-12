package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class Room(
    @SerializedName("id")
    val id: Int, // Room id
    @SerializedName("name")
    val name: String // Room name
)
