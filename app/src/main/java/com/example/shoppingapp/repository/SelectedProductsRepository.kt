package com.example.shoppingapp.repository

import android.content.Context
import androidx.room.Room
import com.example.shoppingapp.data.local.Cart
import com.example.shoppingapp.data.local.Fav
import com.example.shoppingapp.data.local.PreferencesDatabase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class SelectedProductsRepository (context: Context) {

    private val db = Room.databaseBuilder(
        context,
        PreferencesDatabase::class.java,
        "preferences"
    ).build()

    private val userId = Firebase.auth.currentUser!!.uid

    private val cartDao = db.cartDao()
    private val favDao = db.favDao()


    // READ
    suspend fun getCart() : List<Cart> {
        return cartDao.getAll(userId)
    }

    suspend fun getFavs() : List<Fav> {
        return favDao.getAll(userId)
    }

    suspend fun getOneCart(id:String) : Cart? {
       return cartDao.getId(userId,id)
    }

    suspend fun getOneFav(id:String) : Fav? {
        return favDao.getId(userId,id)
    }


    // CREATE + UPDATE
    suspend fun dropInCart(productId: String){
        val cart = Cart(
            productId = productId,
            userId = userId,
            id = 0 // from room
        )
        cartDao.insert(cart)
    }

    suspend fun addToFav(productId: String){
        val fav = Fav(
            productId = productId,
            userId = userId,
            id = 0 // from room
        )
        favDao.insert(fav)
    }

    // DELETE
    suspend fun deleteFromCart(productId:String){
        cartDao.delete(productId)
    }

    suspend fun deleteFromFav(productId:String){
        favDao.delete(productId)
    }




}