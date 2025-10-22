package com.example.shoppingapp.data.model

data class Product(
    var id:String,
    val title:String,
    val description:String,
    val price:String,
    val category: String,
    val images:String,
    val rating:String,
    val userId:String,
)
