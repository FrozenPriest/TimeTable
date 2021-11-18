package com.frozenpriest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.frozenpriest.data.local.converters.ArrayStringConverter
import com.frozenpriest.data.local.dao.RecordsDao
import com.frozenpriest.data.local.entities.AvailablePeriodEntity
import com.frozenpriest.data.local.entities.AvailableStatusEntity
import com.frozenpriest.data.local.entities.AvailableTypeEntity
import com.frozenpriest.data.local.entities.DoctorEntity
import com.frozenpriest.data.local.entities.PatientEntity
import com.frozenpriest.data.local.entities.RecordEntity
import com.frozenpriest.data.local.entities.crossref.RecordAvailableTypeCrossRef

@TypeConverters(ArrayStringConverter::class)
@Database(
    entities = [
        RecordEntity::class,
        PatientEntity::class,
        AvailableStatusEntity::class,
        AvailableTypeEntity::class,
        AvailablePeriodEntity::class,

        RecordAvailableTypeCrossRef::class,

        DoctorEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MyRoomDatabase : RoomDatabase() {
    abstract fun recordsDao(): RecordsDao
}
