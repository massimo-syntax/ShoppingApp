package com.example.shoppingapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Cart::class, Fav::class], version = 1)
abstract class PreferencesDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDAO
    abstract fun favDao(): FavDAO


}