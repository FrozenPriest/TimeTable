package com.frozenpriest

import android.app.Application
import com.frozenpriest.di.app.AppComponent
import com.frozenpriest.di.app.AppModule
import com.frozenpriest.di.app.DaggerAppComponent

class MyApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}
