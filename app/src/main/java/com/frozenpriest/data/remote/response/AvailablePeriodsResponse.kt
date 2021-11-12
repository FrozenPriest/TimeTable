package com.frozenpriest.data.remote.response

import com.google.gson.annotations.SerializedName

// api/demo/periods Массив из всех периодов
data class AvailablePeriodsResponse(
    @SerializedName("available_periods")
    val availablePeriods: List<AvailablePeriod> // list of available periods
)
