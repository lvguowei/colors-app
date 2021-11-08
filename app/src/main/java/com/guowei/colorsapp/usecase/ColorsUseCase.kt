package com.guowei.colorsapp.usecase

import com.guowei.colorsapp.cache.SessionCache
import com.guowei.colorsapp.networking.api.StorageApi
import com.guowei.colorsapp.networking.schema.StorageRequestBody
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ColorsUseCase @Inject constructor(
    private val storageApi: StorageApi,
    private val sessionCache: SessionCache
) {

    /**
     * Get the current color from server if exists, otherwise create with first color in the list.
     */
    fun getOrCreate(): Single<String> =
        Single.fromCallable {
            sessionCache.getStorageId().orEmpty()
        }
            .flatMap { storageId ->
                if (storageId.isBlank()) {
                    storageApi.create(StorageRequestBody(Colors.colorSet[0]))
                        .doOnSuccess { sessionCache.saveStorageId(it.id) }
                        .map { it.data }
                } else {
                    storageApi.get(storageId).map { it.data }
                }
            }
            .retryWhen(RetryWithDelay(maxRetries = 3, delay = 1, unit = TimeUnit.SECONDS))

    fun update(color: String): Single<String> =
        Single.fromCallable {
            sessionCache.getStorageId()
        }
            .flatMap { id ->
                storageApi.update(id, StorageRequestBody(color)).map { it.data }
            }
            .retryWhen(RetryWithDelay(maxRetries = 3, delay = 1, unit = TimeUnit.SECONDS))

    fun getColorSet(): Single<List<String>> = Single.just(Colors.colorSet)
}