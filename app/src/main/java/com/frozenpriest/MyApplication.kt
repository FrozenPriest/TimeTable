package com.frozenpriest

import android.app.Application
import com.frozenpriest.di.app.AppComponent
import com.frozenpriest.di.app.AppModule
import com.frozenpriest.di.app.DaggerAppComponent
import com.frozenpriest.di.app.LocalDataModule
import timber.log.Timber

class MyApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .localDataModule(LocalDataModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
