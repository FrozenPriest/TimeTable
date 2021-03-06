package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

// api/demo/types Массив из всех типов
data class AvailableTypesResponse(
    @SerializedName("results")
    val availableTypes: List<AvailableType>, // list of available types
)
