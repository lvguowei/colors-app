package com.guowei.colorsapp.cache

interface SessionCache {
    fun saveToken(token: String)
    fun getToken(): String?
    fun saveStorageId(id: String)
    fun getStorageId(): String?
    fun clear()
}