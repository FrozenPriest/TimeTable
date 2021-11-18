package com.frozenpriest.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.frozenpriest.domain.model.LocalDoctorSchedule
import com.frozenpriest.domain.usecase.FetchAvailablePeriodsUseCase
import com.frozenpriest.domain.usecase.FetchAvailableStatusesUseCase
import com.frozenpriest.domain.usecase.FetchAvailableTypesUseCase
import com.frozenpriest.domain.usecase.FetchScheduleUseCase
import com.frozenpriest.domain.usecase.GetCurrentDayUseCase
import com.frozenpriest.domain.usecase.caching.CacheAvailablePeriodsUseCase
import com.frozenpriest.domain.usecase.caching.CacheAvailableStatusesUseCase
import com.frozenpriest.domain.usecase.caching.CacheAvailableTypesUseCase
import com.frozenpriest.ui.common.viewmodels.SavedStateViewModel
import com.frozenpriest.utils.getDateOfDayOfWeek
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val getCurrentDayUseCase: GetCurrentDayUseCase,
    private val fetchAvailablePeriodsUseCase: FetchAvailablePeriodsUseCase,
    private val fetchAvailableStatusesUseCase: FetchAvailableStatusesUseCase,
    private val fetchAvailableTypesUseCase: FetchAvailableTypesUseCase,
    private val fetchScheduleUseCase: FetchScheduleUseCase,
    private val cacheAvailablePeriodsUseCase: CacheAvailablePeriodsUseCase,
    private val cacheAvailableStatusesUseCase: CacheAvailableStatusesUseCase,
    private val cacheAvailableTypesUseCase: CacheAvailableTypesUseCase,
) : SavedStateViewModel() {

    private lateinit var _schedule: MutableLiveData<LocalDoctorSchedule>
    val schedule: LiveData<LocalDoctorSchedule> get() = _schedule

    private lateinit var _currentDate: MutableLiveData<Date>
    val currentDate: LiveData<Date> get() = _currentDate

    private lateinit var _weekDatesSorted: MutableLiveData<List<Date>>
    val weekDatesSorted: LiveData<List<Date>> get() = _weekDatesSorted

    private lateinit var _isLoading: MutableLiveData<Boolean>
    val isLoading: LiveData<Boolean> get() = _isLoading

    private lateinit var selectedWeek: Date

    override fun init(savedStateHandle: SavedStateHandle) {
        _schedule = savedStateHandle.getLiveData("schedule")
        _currentDate = savedStateHandle.getLiveData("date")
        _weekDatesSorted = savedStateHandle.getLiveData("weekdatessorted")
        _isLoading = savedStateHandle.getLiveData("isLoading")

        loadDate()

        selectedWeek = if (savedStateHandle.contains("selectedWeek"))
            savedStateHandle.get<Date>("selectedWeek")!!
        else
            _currentDate.value!!

        setWeekDays()
        loadSchedule()
    }

    fun selectWeek(date: Date) {
        selectedWeek = date
        setWeekDays()
        loadSchedule()
    }

    val doctorId = "MI3x9PueHJ"

    private fun loadDate() {
        val currentDateResult = getCurrentDayUseCase()
        _currentDate.value = currentDateResult
    }

    private fun setWeekDays() {
        _weekDatesSorted.value = listOf(
            selectedWeek.getDateOfDayOfWeek(Calendar.MONDAY),
            selectedWeek.getDateOfDayOfWeek(Calendar.TUESDAY),
            selectedWeek.getDateOfDayOfWeek(Calendar.WEDNESDAY),
            selectedWeek.getDateOfDayOfWeek(Calendar.THURSDAY),
            selectedWeek.getDateOfDayOfWeek(Calendar.FRIDAY),
            selectedWeek.getDateOfDayOfWeek(Calendar.SATURDAY),
            selectedWeek.getDateOfDayOfWeek(Calendar.SUNDAY),
        )
    }

    fun loadSchedule() {
        viewModelScope.launch {
            _isLoading.value = true
            val availablePeriods = fetchAvailablePeriodsUseCase()
            availablePeriods.onSuccess { cacheAvailablePeriodsUseCase(it) }
            val availableStatuses = fetchAvailableStatusesUseCase()
            availableStatuses.onSuccess { cacheAvailableStatusesUseCase(it) }
            val availableTypes = fetchAvailableTypesUseCase()
            availableTypes.onSuccess { cacheAvailableTypesUseCase(it) }

            val calendar = Calendar.getInstance()
            calendar.time = selectedWeek

            val result = fetchScheduleUseCase(
                doctorId,
                calendar.get(Calendar.WEEK_OF_MONTH),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR),
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
            _isLoading.value = false
        }
    }
}
