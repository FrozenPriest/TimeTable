package com.frozenpriest.domain.model

import com.frozenpriest.data.remote.response.AvailablePeriod
import com.frozenpriest.data.remote.response.AvailableStatus
import com.frozenpriest.data.remote.response.AvailableType

data class PeriodsStatusesTypes(
    val periods: List<AvailablePeriod>,
    val statuses: List<AvailableStatus>,
    val types: List<AvailableType>
)
