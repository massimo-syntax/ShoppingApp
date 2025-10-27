package com.example.shoppingapp.data.model

data class Message(
    val id: String = "",
    val senderId: String,
    val content: String,
    val conversation :String,
    val timestamp: Long = System.currentTimeMillis()
)
