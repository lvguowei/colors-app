package com.guowei.colorsapp.di

import com.guowei.colorsapp.cache.SessionCache
import com.guowei.colorsapp.networking.AuthInterceptor
import com.guowei.colorsapp.networking.api.StorageApi
import com.guowei.colorsapp.networking.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun retrofit(httpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://54t9f06ot1.execute-api.eu-central-1.amazonaws.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(httpClient)
        .build()


    @Provides
    @Singleton
    fun httpClient(authInterceptor: AuthInterceptor) = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(authInterceptor)
        .build()


    @Provides
    @Singleton
    fun authInterceptor(sessionCache: SessionCache) = AuthInterceptor(sessionCache)

    @Provides
    @Singleton
    fun colorsApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun storageApi(retrofit: Retrofit): StorageApi = retrofit.create(StorageApi::class.java)
}