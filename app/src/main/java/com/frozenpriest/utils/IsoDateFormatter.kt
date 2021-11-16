package com.frozenpriest.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class IsoDateFormatter {

    fun getWeekStartEndDates(): Pair<String, String> {

        val startDate = Date().getDateOfDayOfWeek(Calendar.MONDAY)
        val endDate = Date().getDateOfDayOfWeek(Calendar.SUNDAY)

// 2021-11-319T14:11:21.Nov+0300 //// 2011-08-21T18:02:52.249Z
        return Pair(dateToString(startDate), dateToString(endDate))
    }

    private val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())

    fun dateToString(date: Date): String {
        return format.format(date).split("+")[0]
    }

    fun stringToDate(date: String): Date {
        val newDate = format.parse(date.replace("Z", "+0300")) ?: Date()
        val calendar = Calendar.getInstance()
        calendar.time = newDate
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
}

fun Date.getDateOfDayOfWeek(dayOfWeek: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.firstDayOfWeek = Calendar.MONDAY // Set the starting day of the week
    calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.time
}

fun Date.formatDate(): String {

    val format = SimpleDateFormat("dd LLL", Locale.getDefault())
    return format.format(this)
}
