package com.example.shoppingapp.data.model

data class UiProductWithFieldsFromRoom(
    val id:String,
    val title:String,
    val description: String,
    val images:String,
    val price:String,
    val category: String,
    val rating:String,
    // fields used for room, cart and fav are stored locally
    var cart:Boolean = false,
    var fav: Boolean = false,
)
