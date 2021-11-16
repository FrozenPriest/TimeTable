package com.frozenpriest.ui.common

import androidx.appcompat.app.AppCompatActivity
import com.frozenpriest.MyApplication
import com.frozenpriest.di.presentation.PresentationModule

open class BaseActivity : AppCompatActivity() {
    private val applicationComponent get() = (application as MyApplication).appComponent

    val activityComponent by lazy {
        applicationComponent.newActivityComponentBuilder()
            .activity(this)
            .build()
    }

    private val presentationComponent by lazy {
        activityComponent.newPresentationComponent(PresentationModule(this))
    }

    protected val injector get() = presentationComponent
}
