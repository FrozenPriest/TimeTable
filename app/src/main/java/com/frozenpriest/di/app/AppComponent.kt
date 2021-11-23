package com.frozenpriest.di.app

import com.frozenpriest.di.activity.ActivityComponent
import dagger.Component

@AppScope
@Component(modules = [AppModule::class, LocalDataModule::class])
interface AppComponent {
    fun newActivityComponentBuilder(): ActivityComponent.Builder
}
