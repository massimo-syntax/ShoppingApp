package com.example.shoppingapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.shoppingapp.data.model.Product
import com.example.shoppingapp.data.model.UiProductWithFieldsFromRoom
import com.example.shoppingapp.data.model.User
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ProductsViewModel : ViewModel() {

    private val userId = Firebase.auth.currentUser!!.uid
    private val DB = Firebase.firestore


    private val _uiProducts = MutableStateFlow<List<UiProductWithFieldsFromRoom>>(emptyList())
    val uiProducts = _uiProducts.asStateFlow()

    // some constant

    val PRODUCTS = "products"


    private fun documentToProduct(map:Map<String,Any> , cart:List<String>, favs:List<String>) : UiProductWithFieldsFromRoom {
        val rating = String.format("%.2f" , Random.nextFloat() )
        return UiProductWithFieldsFromRoom(
            id = map["id"].toString(),
            title = map["title"].toString(),
            description = map["description"].toString(),
            images = map["images"].toString(),
            price = map["price"].toString(),
            category = map["category"].toString(),
            rating = (map["rating"] ?: rating) as String,
            cart = cart.contains(map["id"].toString()),
            fav = favs.contains(map["id"].toString()),
        )
    }


    suspend fun getAllProducts( roomRepository: SelectedProductsRepository ){

        // get list of cart and favourites
        val cart = roomRepository.getCart().map { it.productId }
        val favs = roomRepository.getFavs().map { it.productId }

        // create fill a list with product objects capable of cart-fav + toggle
        val ui_Products = mutableListOf<UiProductWithFieldsFromRoom>()

        DB.collection(PRODUCTS)
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    val ui_p = documentToProduct(it.data!! , cart, favs)
                    ui_Products.add(ui_p)
                }
                _uiProducts.value = ui_Products


                Log.wtf("PRODUCTS !!" , _uiProducts.value.toString())

                Log.wtf("PRODUCTS !!" , _uiProducts.value.toString())
                Log.wtf("PRODUCTS !!" , _uiProducts.value.toString())
                Log.wtf("PRODUCTS !!" , _uiProducts.value.toString())
                Log.wtf("PRODUCTS !!" , _uiProducts.value.toString())
                Log.wtf("PRODUCTS !!" , _uiProducts.value.toString())



            }.addOnFailureListener {
                Log.wtf("ERROR FETCHING LIST" , it.message.toString() )
                Log.wtf("ERROR FROM DATABASE FIREBASE" , it.message.toString() )
            }

    }



    @OptIn(ExperimentalUuidApi::class)
    fun uploadProduct(title:String, description:String, price:String, images:String, category: String, onSuccess:()->Unit ){
        val uuid = Uuid.random().toString()
        val p = Product(
            id = uuid,
            title = title,
            description = description,
            price = price,
            images = images,
            rating = "0.0",
            category = category,
            userId = userId
        )
        DB.collection(PRODUCTS)
            .document(uuid)
            .set(p)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    onSuccess()
                }else{
                    // feedback to UI
                }
            }
    }


}