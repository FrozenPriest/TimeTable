package com.frozenpriest.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.RESTRICT
import androidx.room.PrimaryKey

@Entity(
    tableName = "records",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = AvailableStatusEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = RESTRICT
        )
    ]
)
data class RecordEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String, // record id
    @ColumnInfo(name = "status")
    val statusId: String, // available status id
    @ColumnInfo(name = "types")
    val types: List<String>, // available type ids
    @ColumnInfo(name = "patientId")
    val patient: String, // associated patient id
    @ColumnInfo(name = "reason")
    val reason: String, // record's reason
    @ColumnInfo(name = "room")
    val room: String, // associated room
    @ColumnInfo(name = "start")
    val start: Int, // Start time in seconds after 00:00
    @ColumnInfo(name = "end")
    val end: Int, // End time in seconds after 00:00
)
