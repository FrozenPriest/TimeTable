package com.frozenpriest.ui.common

import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.frozenpriest.di.presentation.PresentationModule

class BaseFragment @ContentView constructor(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {
    private val presentationComponent by lazy {
        (requireActivity() as BaseActivity).activityComponent.newPresentationComponent(
            PresentationModule(this)
        )
    }

    protected val injector get() = presentationComponent
}
