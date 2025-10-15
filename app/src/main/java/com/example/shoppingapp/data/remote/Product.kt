package com.example.shoppingapp.data.remote

import android.media.Rating

data class Product(
    val id: String,
    val title: String,
    val price: String,
    val description: String,
    val category: String,
    val image: String,
    val rating: String? = "0.0"
)
