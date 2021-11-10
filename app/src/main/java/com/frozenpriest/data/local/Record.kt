package com.frozenpriest.data.local

import android.content.Context
import androidx.core.content.ContextCompat
import com.frozenpriest.R

data class Record(
    val name: String,
    val time: Int,
    val duration: Int,
    val dividerColor: Int,
    val backgroundColor: Int,
    val type: RecordType
)

fun makeNoShiftRecord(context: Context, time: Int, duration: Int) =
    Record(
        "Нет смены",
        time,
        duration,
        ContextCompat.getColor(context, R.color.divider_empty),
        ContextCompat.getColor(context, R.color.no_record_background),
        RecordType.EMPTY
    )

fun makeEmptyRecord(context: Context, time: Int, duration: Int) =
    Record(
        "Свободно",
        time,
        duration,
        ContextCompat.getColor(context, R.color.divider_no_shift),
        ContextCompat.getColor(context, R.color.divider_no_shift),
        RecordType.EMPTY
    )

enum class RecordType {
    EMPTY, OCCUPIED
}
