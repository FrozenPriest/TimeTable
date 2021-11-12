package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

data class Organization(
    @SerializedName("name")
    val name: String // organization name
)
