package com.example.shoppingapp.data.model

data class Rating(
    var id:String? = null,
    val productId:String? = null,
    val userId:String? = null,
    val rating:Float = 0f,
    val comment:String = "",
    val timestamp: Long = System.currentTimeMillis()
    )
