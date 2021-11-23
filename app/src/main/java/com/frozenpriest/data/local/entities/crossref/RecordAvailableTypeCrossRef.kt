package com.frozenpriest.data.local.entities.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = ["recordId", "availableTypeId"]
)
data class RecordAvailableTypeCrossRef(
    @ColumnInfo(name = "recordId")
    val recordId: String,
    @ColumnInfo(name = "availableTypeId")
    val availableTypeId: String
)
