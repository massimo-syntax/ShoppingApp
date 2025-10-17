package com.example.shoppingapp.features

data class UIState(
    val isUploading: Boolean = false,
    val images: MutableList<ImageResults> = ArrayList()
)

data class ImageResults(val uid: String, val imageUrl: String)