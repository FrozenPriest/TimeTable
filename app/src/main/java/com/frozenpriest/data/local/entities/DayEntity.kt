package com.frozenpriest.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "days")
data class DayEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String, // inner day id
    @ColumnInfo(name = "docId")
    val doctorId: String,
    @ColumnInfo(name = "date")
    val date: Long, // UNIX Timestamp since 1970 00:00
    @ColumnInfo(name = "available_periods")
    val availablePeriods: List<String>, // list of available period IDs
)
