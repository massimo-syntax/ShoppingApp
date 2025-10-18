package com.example.shoppingapp.data.remote

data class RemoteProduct(
    val id: String,
    val title: String,
    val price: String,
    val description: String,
    val category: String,
    val image: String,
    //val rating: String? = "0.0"
)
