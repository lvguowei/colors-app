package com.guowei.colorsapp.cache

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject

class EncryptedSharedPreferenceSessionCache @Inject constructor(
    context: Context
) : SessionCache {

    private val mainKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFERENCE_FILE,
        mainKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun saveToken(token: String) {
        with(sharedPreferences.edit()) {
            putString(KEY_TOKEN, token)
            apply()
        }
    }

    override fun getToken() = sharedPreferences.getString(KEY_TOKEN, null)

    override fun saveStorageId(id: String) {
        with(sharedPreferences.edit()) {
            putString(KEY_STORAGE_ID, id)
            apply()
        }
    }

    override fun getStorageId() = sharedPreferences.getString(KEY_STORAGE_ID, null)

    override fun clear() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

    companion object {
        private const val PREFERENCE_FILE = "shared_preference_session"

        private const val KEY_TOKEN = "token"
        private const val KEY_STORAGE_ID = "storage_id"

    }
}