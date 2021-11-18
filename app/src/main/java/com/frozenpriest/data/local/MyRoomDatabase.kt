package com.frozenpriest.data.local

import androidx.room.Database
import com.frozenpriest.data.local.entities.AvailableStatusEntity
import com.frozenpriest.data.local.entities.AvailableTypeEntity
import com.frozenpriest.data.local.entities.PatientEntity
import com.frozenpriest.data.local.entities.RecordEntity
import com.frozenpriest.data.local.entities.crossref.RecordAvailableTypeCrossRef

@Database(
    entities = [
        RecordEntity::class,
        PatientEntity::class,
        AvailableStatusEntity::class,
        AvailableTypeEntity::class,
        RecordAvailableTypeCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MyRoomDatabase
