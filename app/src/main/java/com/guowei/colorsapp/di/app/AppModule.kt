package com.guowei.colorsapp.di.app

import android.app.Application
import android.content.Context
import com.guowei.colorsapp.cache.SessionCache
import com.guowei.colorsapp.networking.AuthInterceptor
import com.guowei.colorsapp.networking.api.StorageApi
import com.guowei.colorsapp.networking.api.UserApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@Module
class AppModule(private val application: Application) {

    @Provides
    @AppScope
    fun retrofit(httpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl("https://54t9f06ot1.execute-api.eu-central-1.amazonaws.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()
    }

    @Provides
    @AppScope
    fun httpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @AppScope
    fun authInterceptor(sessionCache: SessionCache): AuthInterceptor {
        return AuthInterceptor(sessionCache)
    }

    @Provides
    @AppScope
    fun colorsApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @AppScope
    fun storageApi(retrofit: Retrofit): StorageApi = retrofit.create(StorageApi::class.java)

    @Provides
    @AppScope
    fun context(): Context = application

}