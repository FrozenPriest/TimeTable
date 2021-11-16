package com.frozenpriest.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.frozenpriest.data.local.LocalDoctorSchedule
import com.frozenpriest.domain.usecase.*
import com.frozenpriest.ui.common.viewmodels.SavedStateViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val getCurrentDayUseCase: GetCurrentDayUseCase,
    private val fetchAvailablePeriodsUseCase: FetchAvailablePeriodsUseCase,
    private val fetchAvailableStatusesUseCase: FetchAvailableStatusesUseCase,
    private val fetchAvailableTypesUseCase: FetchAvailableTypesUseCase,
    private val fetchScheduleUseCase: FetchScheduleUseCase,
) : SavedStateViewModel() {

    private lateinit var _schedule: MutableLiveData<LocalDoctorSchedule>
    val schedule: LiveData<LocalDoctorSchedule> get() = _schedule

    private lateinit var _currentDate: MutableLiveData<Date>
    val currentDate: LiveData<Date> get() = _currentDate

    override fun init(savedStateHandle: SavedStateHandle) {
        _schedule = savedStateHandle.getLiveData("schedule")
        _currentDate = savedStateHandle.getLiveData("date")
        loadDate()
        loadSchedule()
    }

    val doctorId = "MI3x9PueHJ"

    private fun loadDate() {
        _currentDate.value = getCurrentDayUseCase()
    }

    fun loadSchedule() {
        viewModelScope.launch {
            val availablePeriods = fetchAvailablePeriodsUseCase()
            val availableStatuses = fetchAvailableStatusesUseCase()
            val availableTypes = fetchAvailableTypesUseCase()

            val result = fetchScheduleUseCase(
                doctorId,
                Calendar.getInstance().get(Calendar.WEEK_OF_MONTH),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.YEAR),
                // later should take default from room
                availablePeriods = availablePeriods.getOrDefault(emptyList()),
                availableStatuses = availableStatuses.getOrDefault(emptyList()),
                availableTypes = availableTypes.getOrDefault(emptyList())
            )

            result.fold(
                onSuccess = { newSchedule ->
                    _schedule.value = newSchedule
                },
                onFailure = { Timber.e("Error loading schedule") }
            )
        }
    }
}
