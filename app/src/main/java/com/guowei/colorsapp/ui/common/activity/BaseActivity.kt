package com.guowei.colorsapp.ui.common.activity

import androidx.appcompat.app.AppCompatActivity
import com.guowei.colorsapp.ColorsApplication
import com.guowei.colorsapp.di.presentation.PresentationModule

open class BaseActivity : AppCompatActivity() {

    private val appComponent get() = (application as ColorsApplication).appComponent

    val activityComponent by lazy {
        appComponent.newActivityComponentBuilder()
            .activity(this)
            .build()
    }

    private val presentationComponent by lazy {
        activityComponent.newPresentationComponent(
            PresentationModule(
                savedStateRegistryOwner = this,
                args = intent.extras
            )
        )
    }

    protected val injector get() = presentationComponent
}