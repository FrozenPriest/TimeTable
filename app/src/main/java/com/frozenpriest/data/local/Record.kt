package com.frozenpriest.data.local

data class Record(
    val name: String,
    val time: Int,
    val duration: Int,
    val dividerColor: Int,
    val backgroundColor: Int,
    val type: RecordType
)

fun makeNoShiftRecord(time: Int, duration: Int) =
    Record("Нет смены", time, duration, 0xD8D8D8, 0xF0F0F0, RecordType.EMPTY)

fun makeEmptyRecord(time: Int, duration: Int) =
    Record("Свободно", time, duration, 0xF2F2F2, 0xF2F2F2, RecordType.EMPTY)

enum class RecordType {
    EMPTY, OCCUPIED
}
