package com.frozenpriest.ui.common

import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class DialogManager @Inject constructor(private val fragmentManager: FragmentManager) {

    fun showDatePicker(title: String, callback: (Date) -> Unit) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(title)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker.addOnPositiveButtonClickListener {
            val cal = Calendar.getInstance()
            cal.timeInMillis = it

            callback(cal.time)
        }
        datePicker.show(fragmentManager, "week")
    }
}
