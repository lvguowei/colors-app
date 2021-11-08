package com.guowei.colorsapp.networking.api

import com.guowei.colorsapp.networking.schema.LoginRequestBody
import com.guowei.colorsapp.networking.schema.LoginResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("v1/login")
    fun login(@Body body: LoginRequestBody): Single<LoginResponse>
}