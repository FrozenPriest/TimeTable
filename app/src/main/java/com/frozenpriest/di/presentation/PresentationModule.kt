package com.frozenpriest.di.presentation

import androidx.savedstate.SavedStateRegistryOwner
import com.frozenpriest.data.remote.DoctorScheduleApi
import com.frozenpriest.domain.usecase.DummyFetchQuestionUseCase
import com.frozenpriest.domain.usecase.FetchAvailablePeriodsUseCase
import com.frozenpriest.domain.usecase.FetchAvailablePeriodsUseCaseImpl
import com.frozenpriest.domain.usecase.FetchAvailableStatusesUseCase
import com.frozenpriest.domain.usecase.FetchAvailableStatusesUseCaseImpl
import com.frozenpriest.domain.usecase.FetchAvailableTypesUseCase
import com.frozenpriest.domain.usecase.FetchAvailableTypesUseCaseImpl
import com.frozenpriest.domain.usecase.FetchScheduleUseCase
import com.frozenpriest.domain.usecase.FormatScheduleUseCase
import com.frozenpriest.domain.usecase.FormatScheduleUseCaseImpl
import com.frozenpriest.domain.usecase.GetCurrentDayUseCase
import com.frozenpriest.domain.usecase.GetCurrentDayUseCaseImpl
import dagger.Module
import dagger.Provides

@Module
class PresentationModule(private val savedStateRegistryOwner: SavedStateRegistryOwner) {
    @Provides
    fun savedStateRegistryOwner() = savedStateRegistryOwner

    @Provides
    fun provideGetCurrentDateUseCase(): GetCurrentDayUseCase = GetCurrentDayUseCaseImpl()

    @Provides
    fun provideFetchScheduleUseCase(doctorScheduleApi: DoctorScheduleApi): FetchScheduleUseCase =
        DummyFetchQuestionUseCase(doctorScheduleApi)

    @Provides
    fun provideFormatScheduleUseCase(): FormatScheduleUseCase = FormatScheduleUseCaseImpl()

    @Provides
    fun provideFetchAvailableTypesUseCase(doctorScheduleApi: DoctorScheduleApi): FetchAvailableTypesUseCase =
        FetchAvailableTypesUseCaseImpl(doctorScheduleApi)

    @Provides
    fun provideFetchAvailableStatusesUseCase(doctorScheduleApi: DoctorScheduleApi): FetchAvailableStatusesUseCase =
        FetchAvailableStatusesUseCaseImpl(doctorScheduleApi)

    @Provides
    fun provideFetchAvailablePeriodsUseCase(doctorScheduleApi: DoctorScheduleApi): FetchAvailablePeriodsUseCase =
        FetchAvailablePeriodsUseCaseImpl(doctorScheduleApi)
}
