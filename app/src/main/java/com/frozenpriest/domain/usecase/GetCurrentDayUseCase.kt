package com.frozenpriest.domain.usecase

import android.icu.util.GregorianCalendar
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

interface GetCurrentDayUseCase {
    operator fun invoke(): Date
}

class GetCurrentDayUseCaseImpl @Inject constructor() : GetCurrentDayUseCase {
    override fun invoke(): Date {
        val calendar = GregorianCalendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
}
