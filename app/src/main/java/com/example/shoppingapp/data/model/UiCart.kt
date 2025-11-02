package com.example.shoppingapp.data.model

// this data class is usee only in the cart screen
data class UiCart(
    val id:String,
    val title:String,
    val description:String,
    val price:Float,
    val mainPicture:String,
    val quantity:Int = 0
)
