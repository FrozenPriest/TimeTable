package com.frozenpriest.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.frozenpriest.data.remote.response.DoctorSchedule
import com.frozenpriest.domain.usecase.FetchScheduleUseCase
import com.frozenpriest.domain.usecase.GetCurrentDayUseCase
import com.frozenpriest.ui.common.viewmodels.SavedStateViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val getCurrentDayUseCase: GetCurrentDayUseCase,
    private val fetchScheduleUseCase: FetchScheduleUseCase
) : SavedStateViewModel() {

    private lateinit var _schedule: MutableLiveData<DoctorSchedule>
    val schedule: LiveData<DoctorSchedule> get() = _schedule

    private lateinit var _currentDate: MutableLiveData<Date>
    val currentDate: LiveData<Date> get() = _currentDate

    override fun init(savedStateHandle: SavedStateHandle) {
        _schedule = savedStateHandle.getLiveData("schedule")
        _currentDate = savedStateHandle.getLiveData("date")
        loadDate()
        loadSchedule()
    }

    val doctorId = 1

    private fun loadDate() {
        _currentDate.value = getCurrentDayUseCase()
    }

    fun loadSchedule() {
        viewModelScope.launch {
            val result = fetchScheduleUseCase(
                doctorId,
                Calendar.getInstance().get(Calendar.WEEK_OF_MONTH),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.YEAR),
            )

            result.fold(
                onSuccess = { _schedule.value = it },
                onFailure = { Timber.e("Error loading schedule") }
            )
        }
    }
}
