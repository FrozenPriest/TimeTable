package com.frozenpriest.data.local.entities.query

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.frozenpriest.data.local.entities.AvailableStatusEntity
import com.frozenpriest.data.local.entities.AvailableTypeEntity
import com.frozenpriest.data.local.entities.PatientEntity
import com.frozenpriest.data.local.entities.RecordEntity
import com.frozenpriest.data.local.entities.crossref.RecordAvailableTypeCrossRef

data class FullRecord(
    @Embedded val recordEntity: RecordEntity,
    @Relation(
        parentColumn = "status",
        entityColumn = "id"
    ) val statusEntity: AvailableStatusEntity,
    @Relation(
        parentColumn = "patientId",
        entityColumn = "id"
    ) val patientEntity: PatientEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            RecordAvailableTypeCrossRef::class,
            parentColumn = "recordId",
            entityColumn = "availableTypeId"
        )

    )
    val availableTypes: List<AvailableTypeEntity>
)
