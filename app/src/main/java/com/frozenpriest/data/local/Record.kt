package com.frozenpriest.data.local

import com.frozenpriest.data.remote.response.AvailableStatus
import com.frozenpriest.data.remote.response.AvailableType
import com.frozenpriest.data.remote.response.Patient

data class Record(
    val id: String?, // record id
    val status: AvailableStatus?, // available status
    val types: List<AvailableType>?, // available type ids
    val patient: Patient?, // associated patient
    val reason: String?, // record's reason
    val room: String?, // associated room
    val start: Int, // Start time in seconds after 00:00
    val end: Int, // End time in seconds after 00:00
    val recordType: RecordType,
    val backgroundColor: Int?,
    val dividerColor: Int?
)

// fun makeNoShiftRecord(context: Context, time: Int, duration: Int) =
//    Record(
//        "Нет смены",
//        time,
//        duration,
//        ContextCompat.getColor(context, R.color.divider_empty),
//        ContextCompat.getColor(context, R.color.no_record_background),
//        RecordType.EMPTY
//    )
//
// fun makeEmptyRecord(context: Context, time: Int, duration: Int) =
//    Record(
//        "Свободно",
//        time,
//        duration,
//        ContextCompat.getColor(context, R.color.divider_no_shift),
//        ContextCompat.getColor(context, R.color.divider_no_shift),
//        RecordType.EMPTY
//    )

enum class RecordType {
    EMPTY, OCCUPIED
}
