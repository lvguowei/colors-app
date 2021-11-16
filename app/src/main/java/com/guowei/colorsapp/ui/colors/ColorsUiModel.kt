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
) : Parcelable {

    @IgnoredOnParcel
    val currentIndex by lazy {
        colorSet?.indexOf(currentColorLocal) ?: -1
    }

    @IgnoredOnParcel
    val isAtFirst by lazy {
        colorSet?.firstOrNull() == currentColorLocal
    }

    @IgnoredOnParcel
    val isAtLast by lazy {
        colorSet?.lastOrNull() == currentColorLocal
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
    val next by lazy {
        colorSet?.let {
            if (isAtLast) {
                null
            } else {
                copy(currentColorLocal = it[currentIndex + 1])
            }
        }
    }

    @IgnoredOnParcel
    val previous by lazy {
        colorSet?.let {
            if (isAtFirst) {
                null
            } else {
                copy(currentColorLocal = it[currentIndex - 1])
            }
        }
    }

    companion object {
        val Empty: ColorsUiModel = ColorsUiModel(null, null, null)
    }
}