package com.guowei.colorsapp.di.presentation

import com.guowei.colorsapp.ui.colors.ColorsActivity
import com.guowei.colorsapp.ui.login.LoginActivity
import com.guowei.colorsapp.ui.splash.SplashActivity
import dagger.Subcomponent

@PresentationScope
@Subcomponent(modules = [PresentationModule::class, ViewModelModule::class])
interface PresentationComponent {
    fun inject(activity: SplashActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: ColorsActivity)
}