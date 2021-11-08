package com.guowei.colorsapp.ui.common.fragment

import androidx.fragment.app.Fragment
import com.guowei.colorsapp.di.presentation.PresentationModule
import com.guowei.colorsapp.ui.common.activity.BaseActivity


open class BaseFragment : Fragment() {

    private val presentationComponent by lazy {
        (requireActivity() as BaseActivity).activityComponent.newPresentationComponent(
            PresentationModule(savedStateRegistryOwner = this, args = arguments)
        )
    }

    protected val injector get() = presentationComponent
}