package com.frozenpriest.di.presentation

import androidx.fragment.app.FragmentManager
import androidx.savedstate.SavedStateRegistryOwner
import com.frozenpriest.data.local.dao.RecordsDao
import com.frozenpriest.data.remote.DoctorScheduleApi
import com.frozenpriest.data.remote.RemoteRepository
import com.frozenpriest.data.remote.RemoteRepositoryImpl
import com.frozenpriest.domain.RecordCreator
import com.frozenpriest.domain.usecase.CacheInDatabaseUseCase
import com.frozenpriest.domain.usecase.CacheInDatabaseUseCaseImpl
import com.frozenpriest.domain.usecase.FetchScheduleUseCase
import com.frozenpriest.domain.usecase.FetchScheduleUseCaseImpl
import com.frozenpriest.domain.usecase.GetCurrentDayUseCase
import com.frozenpriest.domain.usecase.GetCurrentDayUseCaseImpl
import com.frozenpriest.domain.usecase.LoadCachedUseCase
import com.frozenpriest.domain.usecase.LoadCachedUseCaseImpl
import com.frozenpriest.ui.common.DialogManager
import dagger.Module
import dagger.Provides

@Module
class PresentationModule(private val savedStateRegistryOwner: SavedStateRegistryOwner) {
    @Provides
    fun savedStateRegistryOwner() = savedStateRegistryOwner

    @Provides
    fun provideRecordCreator(): RecordCreator = RecordCreator()

    @Provides
    fun provideGetCurrentDateUseCase(): GetCurrentDayUseCase = GetCurrentDayUseCaseImpl()

    @Provides
    fun provideFetchScheduleUseCase(
        doctorScheduleApi: DoctorScheduleApi,
        recordCreator: RecordCreator
    ): FetchScheduleUseCase =
        FetchScheduleUseCaseImpl(doctorScheduleApi, recordCreator)

    @Provides
    fun provideRemoteRepository(doctorScheduleApi: DoctorScheduleApi): RemoteRepository =
        RemoteRepositoryImpl(doctorScheduleApi)

    @Provides
    fun provideCacheInDatabaseUseCase(dao: RecordsDao): CacheInDatabaseUseCase =
        CacheInDatabaseUseCaseImpl(dao)

    @Provides
    fun provideLoadCachedUseCase(dao: RecordsDao, recordCreator: RecordCreator): LoadCachedUseCase =
        LoadCachedUseCaseImpl(dao, recordCreator)

    @Provides
    fun provideDialogManager(fragmentManager: FragmentManager): DialogManager =
        DialogManager(fragmentManager)
}
