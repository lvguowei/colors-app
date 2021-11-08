package com.guowei.colorsapp.di.presentation

import androidx.lifecycle.ViewModel
import com.guowei.colorsapp.ui.colors.ColorsViewModel
import com.guowei.colorsapp.ui.login.LoginViewModel
import com.guowei.colorsapp.ui.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun splashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ColorsViewModel::class)
    abstract fun colorsViewModel(viewModel: ColorsViewModel): ViewModel

}