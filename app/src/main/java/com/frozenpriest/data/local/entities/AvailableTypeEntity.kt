package com.frozenpriest.data.local.entities

import androidx.room.ColumnInfo

data class AvailableTypeEntity(
    @ColumnInfo(name = "id")
    val id: String, // type id
    @ColumnInfo(name = "name")
    val name: String // type name
)
