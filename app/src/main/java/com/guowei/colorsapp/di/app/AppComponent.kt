package com.guowei.colorsapp.di.app

import com.guowei.colorsapp.di.activity.ActivityComponent
import dagger.Component

@AppScope
@Component(modules = [AppModule::class, CacheModule::class])
interface AppComponent {
    fun newActivityComponentBuilder(): ActivityComponent.Builder
}
