package com.guowei.colorsapp.usecase

import com.guowei.colorsapp.cache.SessionCache
import com.guowei.colorsapp.networking.api.StorageApi
import com.guowei.colorsapp.networking.api.UserApi
import com.guowei.colorsapp.networking.schema.LoginRequestBody
import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val userApi: UserApi,
    private val storageApi: StorageApi,
    private val sessionCache: SessionCache
) {
    fun isLoggedIn(): Single<Boolean> = Single.fromCallable {
        sessionCache.getToken() != null
    }

    fun login(username: String, password: String): Completable =
        userApi.login(LoginRequestBody(username, password))
            .doOnSuccess {
                sessionCache.saveToken(it.token)
            }
            .ignoreElement()

    fun logout(): Completable =
        Single.fromCallable {
            sessionCache.getStorageId()
        }
            .flatMapCompletable {
                storageApi.delete(it)
            }
            .retryWhen(RetryWithDelay(maxRetries = 3, delay = 1, unit = TimeUnit.SECONDS))
            .doOnComplete {
                sessionCache.clear()
            }
}
