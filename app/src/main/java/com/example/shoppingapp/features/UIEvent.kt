package com.example.shoppingapp.features

import android.content.Context
import android.net.Uri

sealed class UIEvent {
    data class SingleImageChanged(val uri: Uri, val context: Context) : UIEvent()
}