package com.frozenpriest.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AvailablePeriodEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String, // status id
    @ColumnInfo(name = "start")
    val start: Int, // Start time in seconds after 00:00
    @ColumnInfo(name = "end")
    val end: Int // End time in seconds after 00:00
)
