package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

// api/demo/periods Массив из всех периодов
data class AvailablePeriodsResponse(
    @SerializedName("results")
    val availablePeriods: List<AvailablePeriod> // list of available periods
)
