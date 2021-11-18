package com.frozenpriest.data.local.converters

import com.frozenpriest.data.local.entities.AvailablePeriodEntity
import com.frozenpriest.data.local.entities.AvailableStatusEntity
import com.frozenpriest.data.local.entities.AvailableTypeEntity
import com.frozenpriest.data.remote.response.AvailablePeriod
import com.frozenpriest.data.remote.response.AvailableStatus
import com.frozenpriest.data.remote.response.AvailableType

fun AvailablePeriod.toEntity(): AvailablePeriodEntity {
    return AvailablePeriodEntity(
        id = this.id,
        start = this.start,
        end = this.end
    )
}

fun AvailableStatus.toEntity(): AvailableStatusEntity {
    return AvailableStatusEntity(
        id = this.id,
        name = this.name
    )
}

fun AvailableType.toEntity(): AvailableTypeEntity {
    return AvailableTypeEntity(
        id = this.id,
        name = this.name
    )
}
