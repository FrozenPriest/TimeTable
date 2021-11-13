package com.frozenpriest.di.presentation

import com.frozenpriest.ui.MainActivity
import com.frozenpriest.ui.calendar.CalendarFragment
import dagger.Subcomponent

@PresentationScope
@Subcomponent(modules = [PresentationModule::class, ViewModelModule::class])
interface PresentationComponent {
    fun inject(fragment: CalendarFragment)
    fun inject(activity: MainActivity)
}
