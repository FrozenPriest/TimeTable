package com.frozenpriest.data.local

import java.util.*

data class LocalDaySchedule(
    val id: String, // inner day id
    val date: Date, // UNIX Timestamp since 1970 00:00
    val records: List<Record> // list of associated records including not occupied
)
