package com.guowei.colorsapp

import android.app.Application
import com.guowei.colorsapp.di.app.AppComponent
import com.guowei.colorsapp.di.app.AppModule
import com.guowei.colorsapp.di.app.DaggerAppComponent

class ColorsApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}