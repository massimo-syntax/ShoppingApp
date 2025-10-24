package com.example.shoppingapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.shoppingapp.Category
import com.example.shoppingapp.data.model.Product
import com.example.shoppingapp.data.model.UiProductWithFieldsFromRoom
import com.example.shoppingapp.data.model.User
import com.example.shoppingapp.features.ImageResults
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ProductsViewModel : ViewModel() {

    data class UIState(
        val fetching: Boolean = true,
        val recomposition:Int = 0,
        val result: List<UiProductWithFieldsFromRoom> = emptyList()
    )

    private val userId = Firebase.auth.currentUser!!.uid
    private val DB = Firebase.firestore


    private val _uiProducts = MutableStateFlow(UIState())
    val uiProducts: StateFlow<UIState> = _uiProducts.asStateFlow()

    // some constant

    val PRODUCTS = "products"
    val productsRef = DB.collection(PRODUCTS)



    private fun documentToProduct(
        map: Map<String, Any>,
        cart: List<String>,
        favs: List<String>
    ): UiProductWithFieldsFromRoom {
        val rating = String.format("%.2f", Random.nextFloat())
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

    fun updateList(index:Int,product: UiProductWithFieldsFromRoom){

        val list = _uiProducts.value.result.toMutableList()
        list[index] = product
        _uiProducts.update { it.copy(false, it.recomposition+1,list) }

    }

    suspend fun getAllProducts(roomRepository: SelectedProductsRepository) {

        // get list of cart and favourites
        val cart = roomRepository.getCart().map { it.productId }
        val favs = roomRepository.getFavs().map { it.productId }

        // create fill a list with product objects capable of cart-fav + toggle
        val uiProducts = mutableListOf<UiProductWithFieldsFromRoom>()

        productsRef
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    val ui_p = documentToProduct(it.data!!, cart, favs)
                    uiProducts.add(ui_p)
                }

                _uiProducts.value = UIState(fetching = false, result = uiProducts)
            }
            .addOnFailureListener {
                Log.wtf("ERROR FETCHING LIST", it.message.toString())
                Log.wtf("ERROR FROM DATABASE FIREBASE", it.message.toString())
            }

    }

    fun getProduct(id:String){
        productsRef
            .document(id)
            .get()
            .addOnSuccessListener {
                val product = documentToProduct(it.data!!, emptyList(),emptyList())
                _uiProducts.value = UIState(fetching = false, result = mutableListOf(product) )
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR FETCHING DATA FORM FIREBASE, CATEGORY QUERY", "Error getting documents: ", exception)
            }
    }

    fun getCategory(category: Category) {
        // create fill a list with product objects capable of cart-fav + toggle
        val ui_Products = mutableListOf<UiProductWithFieldsFromRoom>()

        productsRef
            .whereEqualTo("category", category.enam)
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    val ui_p = documentToProduct(it.data!!, emptyList(), emptyList())
                    ui_Products.add(ui_p)
                }
                _uiProducts.value = UIState(fetching = false, result = ui_Products)

            }
            .addOnFailureListener { exception ->
                Log.w("ERROR FETCHING DATA FORM FIREBASE, CATEGORY QUERY", "Error getting documents: ", exception)
            }




    }


    @OptIn(ExperimentalUuidApi::class)
    fun uploadProduct(
        title: String,
        description: String,
        price: String,
        images: String,
        category: String,
        onSuccess: () -> Unit
    ) {
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
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    // feedback to UI
                }
            }
    }


}