package com.example.shoppingapp.pictureUpload

data class UIState(
    val isUploading: Boolean = false,
    val images: MutableList<ImageResults> = ArrayList()
)

data class ImageResults(val uid: String, val imageUrl: String)