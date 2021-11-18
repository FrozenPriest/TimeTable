package com.frozenpriest.data.local.entities.query

import androidx.room.Embedded
import androidx.room.Relation
import com.frozenpriest.data.local.entities.DoctorEntity

data class DoctorWithRecords(
    @Embedded val doctor: DoctorEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    ) val fullRecords: List<FullRecord>,
)
