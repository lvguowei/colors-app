package com.guowei.colorsapp.networking.api

import com.guowei.colorsapp.networking.schema.StorageRequestBody
import com.guowei.colorsapp.networking.schema.StorageResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface StorageApi {

    @POST("v1/storage")
    fun create(@Body data: StorageRequestBody): Single<StorageResponse>

    @GET("v1/storage/{id}")
    fun get(@Path("id") id: String): Single<StorageResponse>

    @PUT("v1/storage/{id}")
    fun update(@Path("id") id: String, @Body data: StorageRequestBody): Single<StorageResponse>

    @DELETE("v1/storage/{id}")
    fun delete(@Path("id") id: String): Completable

}