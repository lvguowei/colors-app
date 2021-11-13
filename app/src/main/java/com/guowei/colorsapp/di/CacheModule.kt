package com.guowei.colorsapp.di

import com.guowei.colorsapp.cache.EncryptedSharedPreferenceSessionCache
import com.guowei.colorsapp.cache.SessionCache
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Binds
    @Singleton
    abstract fun sessionCache(cache: EncryptedSharedPreferenceSessionCache): SessionCache
}
