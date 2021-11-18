package com.frozenpriest.di.presentation

import androidx.fragment.app.FragmentManager
import androidx.savedstate.SavedStateRegistryOwner
import com.frozenpriest.data.local.dao.RecordsDao
import com.frozenpriest.data.remote.DoctorScheduleApi
import com.frozenpriest.domain.usecase.FetchAvailablePeriodsUseCase
import com.frozenpriest.domain.usecase.FetchAvailablePeriodsUseCaseImpl
import com.frozenpriest.domain.usecase.FetchAvailableStatusesUseCase
import com.frozenpriest.domain.usecase.FetchAvailableStatusesUseCaseImpl
import com.frozenpriest.domain.usecase.FetchAvailableTypesUseCase
import com.frozenpriest.domain.usecase.FetchAvailableTypesUseCaseImpl
import com.frozenpriest.domain.usecase.FetchScheduleUseCase
import com.frozenpriest.domain.usecase.FetchScheduleUseCaseImpl
import com.frozenpriest.domain.usecase.GetCurrentDayUseCase
import com.frozenpriest.domain.usecase.GetCurrentDayUseCaseImpl
import com.frozenpriest.domain.usecase.caching.CacheAvailablePeriodsUseCase
import com.frozenpriest.domain.usecase.caching.CacheAvailablePeriodsUseCaseImpl
import com.frozenpriest.domain.usecase.caching.CacheAvailableStatusesUseCase
import com.frozenpriest.domain.usecase.caching.CacheAvailableStatusesUseCaseImpl
import com.frozenpriest.domain.usecase.caching.CacheAvailableTypesUseCase
import com.frozenpriest.domain.usecase.caching.CacheAvailableTypesUseCaseImpl
import com.frozenpriest.ui.common.DialogManager
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
        FetchScheduleUseCaseImpl(doctorScheduleApi)

    @Provides
    fun provideFetchAvailableTypesUseCase(doctorScheduleApi: DoctorScheduleApi): FetchAvailableTypesUseCase =
        FetchAvailableTypesUseCaseImpl(doctorScheduleApi)

    @Provides
    fun provideFetchAvailableStatusesUseCase(doctorScheduleApi: DoctorScheduleApi): FetchAvailableStatusesUseCase =
        FetchAvailableStatusesUseCaseImpl(doctorScheduleApi)

    @Provides
    fun provideFetchAvailablePeriodsUseCase(doctorScheduleApi: DoctorScheduleApi): FetchAvailablePeriodsUseCase =
        FetchAvailablePeriodsUseCaseImpl(doctorScheduleApi)

    @Provides
    fun provideCacheAvailablePeriodsUseCase(dao: RecordsDao): CacheAvailablePeriodsUseCase =
        CacheAvailablePeriodsUseCaseImpl(dao)

    @Provides
    fun provideCacheAvailableStatusesUseCase(dao: RecordsDao): CacheAvailableStatusesUseCase =
        CacheAvailableStatusesUseCaseImpl(dao)

    @Provides
    fun provideCacheAvailableTypesUseCase(dao: RecordsDao): CacheAvailableTypesUseCase =
        CacheAvailableTypesUseCaseImpl(dao)

    @Provides
    fun provideDialogManager(fragmentManager: FragmentManager): DialogManager =
        DialogManager(fragmentManager)
}
