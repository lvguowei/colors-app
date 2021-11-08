package com.guowei.colorsapp.di.presentation

import android.os.Bundle
import androidx.savedstate.SavedStateRegistryOwner
import dagger.Module
import dagger.Provides

@Module
class PresentationModule(
    private val savedStateRegistryOwner: SavedStateRegistryOwner,
    private val args: Bundle?
) {

    @Provides
    fun savedStateRegistryOwner() = savedStateRegistryOwner

    @Provides
    fun args() = args
}