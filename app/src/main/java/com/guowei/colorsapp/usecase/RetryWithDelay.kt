package com.guowei.colorsapp.usecase

import io.reactivex.Flowable
import io.reactivex.functions.Function
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit

class RetryWithDelay(
    private val maxRetries: Int,
    private val delay: Long,
    private val unit: TimeUnit
) : Function<Flowable<Throwable>, Publisher<*>> {

    private var retryCount: Int = 0

    override fun apply(t: Flowable<Throwable>): Publisher<*> {
        return t.flatMap { throwable ->
            return@flatMap if (++retryCount < maxRetries)
                Flowable.timer(delay, unit)
            else
                Flowable.error<Any>(throwable)
        }
    }
}