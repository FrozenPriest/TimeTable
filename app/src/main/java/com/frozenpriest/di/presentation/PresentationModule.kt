package com.frozenpriest.di.presentation

import androidx.savedstate.SavedStateRegistryOwner
import com.frozenpriest.data.remote.DoctorScheduleApi
import com.frozenpriest.domain.usecase.DummyFetchQuestionUseCase
import com.frozenpriest.domain.usecase.FetchScheduleUseCase
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
}
