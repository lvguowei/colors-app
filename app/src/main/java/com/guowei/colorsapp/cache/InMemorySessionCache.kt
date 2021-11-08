package com.guowei.colorsapp.cache

import javax.inject.Inject

class InMemorySessionCache @Inject constructor() : SessionCache {

    private var token: String? = null

    private var storageId: String? = null

    override fun saveToken(token: String) {
        this.token = token
    }

    override fun getToken() = token

    override fun saveStorageId(id: String) {
        this.storageId = id
    }

    override fun getStorageId() = storageId

    override fun clear() {
        token = null
        storageId = null
    }
}