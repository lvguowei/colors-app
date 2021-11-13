package com.guowei.colorsapp.ui.colors

import android.graphics.Color
import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ColorsUiModel(
    // current color on the server
    val currentColorServer: String?,
    // color displayed currently chosen by user
    val currentColorLocal: String?,
    val colorSet: List<String>?,
    val isLoading: Boolean
) : Parcelable {

    @IgnoredOnParcel
    private val currentIndex by lazy {
        colorSet?.indexOf(currentColorLocal) ?: -1
    }

    @IgnoredOnParcel
    val prevButtonVisible by lazy {
        !isLoading && currentIndex > 0
    }

    @IgnoredOnParcel
    val nextButtonVisible by lazy {
        !isLoading && colorSet?.let {
            currentIndex < it.size - 1
        } ?: false
    }

    @IgnoredOnParcel
    val bgColor by lazy {
        if (currentColorLocal == null) {
            // Display white when no user selection yet
            Color.WHITE
        } else {
            Color.parseColor(currentColorLocal)
        }
    }


    @IgnoredOnParcel
    val setButtonVisible by lazy {
        !isLoading && !currentColorServer.equals(currentColorLocal, ignoreCase = true)
    }

    @IgnoredOnParcel
    val next by lazy {
        colorSet?.let {
            if (currentIndex == it.size - 1) {
                null
            } else {
                copy(currentColorLocal = it[currentIndex + 1])
            }
        }
    }

    @IgnoredOnParcel
    val previous by lazy {
        colorSet?.let {
            if (currentIndex == 0) {
                null
            } else {
                copy(currentColorLocal = it[currentIndex - 1])
            }
        }
    }
}