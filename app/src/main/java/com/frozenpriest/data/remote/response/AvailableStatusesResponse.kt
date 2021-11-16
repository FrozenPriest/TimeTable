package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

// api/demo/statuses Масств из всех статусов
data class AvailableStatusesResponse(
    @SerializedName("results")
    val availableStatuses: List<AvailableStatus>, // list of available statuses
)
