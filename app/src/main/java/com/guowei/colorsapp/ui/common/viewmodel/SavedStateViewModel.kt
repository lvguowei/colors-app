package com.guowei.colorsapp.ui.common.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class SavedStateViewModel : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    abstract fun init(savedStateHandle: SavedStateHandle)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    protected fun Disposable.addToDisposable() {
        compositeDisposable.add(this)
    }
}