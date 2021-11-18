package com.frozenpriest.data.local.entities.crossref

import androidx.room.Entity
import androidx.room.ForeignKey
import com.frozenpriest.data.local.entities.AvailableTypeEntity
import com.frozenpriest.data.local.entities.RecordEntity

@Entity(
    primaryKeys = ["recordId", "availableTypeId"],
    foreignKeys = [
        ForeignKey(
            entity = RecordEntity::class,
            parentColumns = ["id"],
            childColumns = ["recordId"]
        ),
        ForeignKey(
            entity = AvailableTypeEntity::class,
            parentColumns = ["id"],
            childColumns = ["availableTypeId"]
        )
    ]
)
data class RecordAvailableTypeCrossRef(
    val recordId: String,
    val availableTypeId: String
)
