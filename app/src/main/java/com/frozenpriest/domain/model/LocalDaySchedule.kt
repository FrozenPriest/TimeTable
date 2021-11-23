package com.frozenpriest.domain.model

import com.frozenpriest.data.remote.response.AvailablePeriod
import java.util.Date

data class LocalDaySchedule(
    val id: String, // inner day id
    val date: Date, // UNIX Timestamp since 1970 00:00
    val availablePeriods: List<AvailablePeriod>,
    val records: List<Record> // list of associated records including not occupied
)
