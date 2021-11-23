package com.frozenpriest.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PatientEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String, // patient inner id
    @ColumnInfo(name = "med_id")
    val medId: String, // patient medicine id
    @ColumnInfo(name = "name")
    val name: String, // patient name
    @ColumnInfo(name = "phone")
    val phone: String // patient phone number
)
