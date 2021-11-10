package com.frozenpriest.utils

object TextUtils {
    fun formatTimePeriod(time: Int, duration: Int): String {
        return formatTime(time) + " - " + formatTime(time + duration)
    }

    @Suppress("MagicNumber")
    fun formatTime(time: Int): String {
        val hour = time / 3600
        val minute = (time - hour * 3600) / 60
        return "%02d:%02d".format(hour, minute)
    }
}
