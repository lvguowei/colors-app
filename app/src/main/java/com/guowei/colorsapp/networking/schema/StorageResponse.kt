package com.guowei.colorsapp.networking.schema

import com.google.gson.annotations.SerializedName

data class StorageResponse(
    @SerializedName("id") val id: String,
    @SerializedName("data") val data: String
)