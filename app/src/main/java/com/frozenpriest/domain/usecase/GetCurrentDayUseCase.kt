package com.frozenpriest.domain.usecase

import android.icu.util.GregorianCalendar
import java.util.Date
import javax.inject.Inject

interface GetCurrentDayUseCase {
    operator fun invoke(): Date
}

class GetCurrentDayUseCaseImpl @Inject constructor() : GetCurrentDayUseCase {
    override fun invoke(): Date {
        return GregorianCalendar.getInstance().time
    }
}
