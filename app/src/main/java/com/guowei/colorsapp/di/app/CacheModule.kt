package com.guowei.colorsapp.di.app

import com.guowei.colorsapp.cache.EncryptedSharedPreferenceSessionCache
import com.guowei.colorsapp.cache.SessionCache
import dagger.Binds
import dagger.Module

@Module
abstract class CacheModule {

    @AppScope
    @Binds
    abstract fun sessionCache(cache: EncryptedSharedPreferenceSessionCache): SessionCache
}
