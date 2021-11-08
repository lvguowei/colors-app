package com.guowei.colorsapp.networking.schema

import com.google.gson.annotations.SerializedName

data class StorageRequestBody(
    @SerializedName("data") val data: String
)