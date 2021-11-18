package com.frozenpriest.data.local.converters

import com.frozenpriest.data.local.entities.AvailablePeriodEntity
import com.frozenpriest.data.remote.response.AvailablePeriod

fun AvailablePeriod.toEntity(): AvailablePeriodEntity {
    return AvailablePeriodEntity(
        id = this.id,
        start = this.start,
        end = this.end
    )
}
