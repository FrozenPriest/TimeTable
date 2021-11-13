package com.frozenpriest.di.activity

import androidx.appcompat.app.AppCompatActivity
import com.frozenpriest.di.presentation.PresentationComponent
import com.frozenpriest.di.presentation.PresentationModule
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun newPresentationComponent(presentationModule: PresentationModule): PresentationComponent

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun activity(activity: AppCompatActivity): Builder
        fun build(): ActivityComponent
    }
}
