package com.frozenpriest.data.local

data class LocalDaySchedule(
    val id: Int, // inner day id
    val date: Int, // UNIX Timestamp since 1970 00:00
    val records: List<Record> // list of associated records including not occupied
)
