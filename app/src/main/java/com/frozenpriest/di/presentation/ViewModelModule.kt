package com.frozenpriest.di.presentation

import androidx.lifecycle.ViewModel
import com.frozenpriest.ui.calendar.CalendarViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CalendarViewModel::class)
    abstract fun myViewModel(myViewModel: CalendarViewModel): ViewModel
}
