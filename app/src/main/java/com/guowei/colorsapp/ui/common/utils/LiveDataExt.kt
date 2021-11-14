package com.guowei.colorsapp.ui.common.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <A : Any, B : Any, R : Any> LiveData<A>.combineLatest(
    otherSource: LiveData<B>, combineFunction: (A, B) -> R
): LiveData<R> {
    return MediatorLiveData<R>().apply {
        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            if (localLastA != null && localLastB != null)
                this.value = combineFunction(localLastA, localLastB)
        }

        addSource(this@combineLatest) {
            if (lastA !== it) {
                lastA = it
                update()
            }

        }
        addSource(otherSource) {
            if (lastB !== it) {
                lastB = it
                update()
            }

        }
    }
}