package com.example.shoppingapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String="",
    val email: String="",
    val name: String="Deleted User",
    val image: String = "",
    val chat : MutableMap<String,String> = mutableMapOf(),
    val ratings: List<String> = listOf()
)
