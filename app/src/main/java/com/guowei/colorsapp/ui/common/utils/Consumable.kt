package com.guowei.colorsapp.ui.common.utils

data class Consumable<out T>(private val content: T) {

    private var hasBeenConsumed = false

    fun consume(onConsume: T.() -> Unit) {
        if (hasBeenConsumed) {
            null
        } else {
            hasBeenConsumed = true
            content
        }?.apply(onConsume)
    }
}

fun <T> T.toConsumable(): Consumable<T> =
    Consumable(this)
