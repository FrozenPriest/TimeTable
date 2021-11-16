package com.frozenpriest.utils

object TextUtils {
    fun formatTimePeriod(time: Int, duration: Int): String {
        return formatTime(time) + " - " + formatTime(time + duration)
    }

    const val secInMinute = 60
    const val minInHour = 60
    const val secInHour = secInMinute * minInHour

    fun formatTime(time: Int): String {
        val hour = time / secInHour
        val minute = (time - hour * secInHour) / secInMinute
        return "%02d:%02d".format(hour, minute)
    }
}
