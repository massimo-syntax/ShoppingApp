package com.example.shoppingapp.viewmodel

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.model.UiCart
import com.example.shoppingapp.data.model.UserSelectedProduct
import com.example.shoppingapp.data.remote.RemoteProduct
import com.example.shoppingapp.repository.RemoteProductsRepository
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserSelectedViewModel() : ViewModel() {

    data class UIState(
        val roomDataLoaded: Boolean = false,
        val firebaseDataLoaded: Boolean = false,
        val list: List<UserSelectedProduct> = emptyList()
    )

    data class CartUiState(
        val roomDataLoaded: Boolean = false,
        val firebaseDataLoaded: Boolean = false,
        val list: List<UiCart> = emptyList()
    )

    private val _uiStateCart = MutableStateFlow(CartUiState())
    val uiStateCart: StateFlow<CartUiState> = _uiStateCart.asStateFlow()

    private val _uiStateFavs = MutableStateFlow(UIState())
    val uiStateFavs: StateFlow<UIState> = _uiStateFavs.asStateFlow()

    private val remoteRepo = RemoteProductsRepository()

    private fun documentToSelectedProduct(map: Map<String, Any>): UserSelectedProduct {

        var mainPicture = ""

        val images = map["images"].toString()
        var title = ""
        if (images == "null" || images == "") {
            title = "Something went wrong with image"
            Log.w("ERROR FETCHING FROM FIREBASE", "images = $images")
        } else {
            mainPicture = images.split(',').first()
        }

        return UserSelectedProduct(
            id = map["id"].toString(),
            title = if (mainPicture.equals("")) title else map["title"].toString(),
            description = map["description"].toString(),
            mainPicture = mainPicture,
            price = map["price"].toString(),
        )
    }



    suspend fun getAllCart(roomRepo: SelectedProductsRepository) {
        // query room database
        val repoCart = roomRepo.getCart()
        val cartIds = repoCart.map { it.productId }
        val cartQuantities = repoCart.map{ it.quantity }
        // separated lists of id s to query source
        val jsonApiIds = mutableListOf<String>()
        val firebaseIds = mutableListOf<String>()
        // separated list from separated source to join
        val jsonApiCart = mutableListOf<UiCart>()
        val firebaseProducts = mutableListOf<UiCart>()
        // joined list
        val completeList = mutableListOf<UiCart>()

        cartIds.forEachIndexed { index, id ->
            if (!id.isDigitsOnly()) { // product is stored in fierbase
                firebaseIds.add(id)
            } else { // product is from json api
                jsonApiIds.add(id)
                val jsonProductRemote: RemoteProduct = remoteRepo.getProduct(id)
                val quantity = cartQuantities[cartIds.indexOf(id)]
                val jsonProduct = UiCart(
                    id = jsonProductRemote.id,
                    title = jsonProductRemote.title,
                    description = jsonProductRemote.description,
                    mainPicture = jsonProductRemote.image,
                    price = jsonProductRemote.price.toFloat(),
                    quantity = quantity
                )
                jsonApiCart.add(jsonProduct)
            }
        }
        completeList.addAll(jsonApiCart)
        _uiStateCart.update { it.copy(roomDataLoaded = true) }

        if (firebaseIds.isEmpty()){
            _uiStateCart.update { it.copy(firebaseDataLoaded = true , list = completeList.toList()) }
            return
        }
        var count = 0
        firebaseIds.forEachIndexed { index , id ->
            Firebase.firestore
                .collection("products")
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (document.data == null) return@addOnSuccessListener
                    val map = document.data!!
                    //val product = documentToSelectedProduct(document.data!!)
                    val quantity = cartQuantities[cartIds.indexOf(id)]
                    val product = UiCart(
                        id = map["id"].toString(),
                        title = map["title"].toString(),
                        description = map["description"].toString(),
                        price = map["price"].toString().toFloat(),
                        mainPicture = map["images"].toString().split(',').first(),
                        quantity = quantity,
                    )

                    firebaseProducts.add(product)

                    count++
                    if (count == firebaseIds.size) {
                        completeList.addAll(firebaseProducts)
                        _uiStateCart.update {
                            it.copy(
                                firebaseDataLoaded = true,
                                list = completeList.toList()
                            )
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(
                        "ERROR FETCHING DATA FORM FIREBASE, CATEGORY QUERY",
                        "Error getting documents: ",
                        exception
                    )
                }
        }


    }


    suspend fun getAllFavs(roomRepo: SelectedProductsRepository) {

        val favsIds = roomRepo.getFavs().map { it.productId }
        val jsonApiIds = mutableListOf<String>()
        val firebaseIds = mutableListOf<String>()
        val jsonApiCart = mutableListOf<UserSelectedProduct>()
        val completeList = mutableListOf<UserSelectedProduct>()

        favsIds.forEach {
            if (!it.isDigitsOnly()) { // product is stored in fierbase
                firebaseIds.add(it)
            } else { // product is from json api
                jsonApiIds.add(it)
                val jsonProductRemote: RemoteProduct = remoteRepo.getProduct(it)
                val jsonProduct = UserSelectedProduct(
                    id = jsonProductRemote.id,
                    title = jsonProductRemote.title,
                    description = jsonProductRemote.description,
                    mainPicture = jsonProductRemote.image,
                    price = jsonProductRemote.price,
                )
                jsonApiCart.add(jsonProduct)
            }
        }
        completeList.addAll(jsonApiCart)

        _uiStateFavs.update { it.copy(roomDataLoaded = true, list = completeList.toList()) }

        if (firebaseIds.isEmpty()){
            _uiStateFavs.update { it.copy(firebaseDataLoaded = true) }
            return
        }

        var count = 0
        val products = mutableListOf<UserSelectedProduct>()

        firebaseIds.forEach { id ->
            Firebase.firestore
                .collection("products")
                .document(id)
                .get()
                .addOnSuccessListener { document ->

                    if (document.data == null) return@addOnSuccessListener

                    val product = documentToSelectedProduct(document.data!!)
                    products.add(product)
                    count++

                    if (count == firebaseIds.size) {
                        completeList.addAll(products)
                        _uiStateFavs.update {
                            it.copy(
                                firebaseDataLoaded = true,
                                list = completeList.toList()
                            )
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(
                        "ERROR FETCHING DATA FORM FIREBASE, CATEGORY QUERY",
                        "Error getting documents: ",
                        exception
                    )
                }
        }

    }


    fun deleteFromFav(repo: SelectedProductsRepository , id:String){
        viewModelScope.launch {
            repo.deleteFromFav(id)
            val list = _uiStateFavs.value.list
            val fav = list.find { it.id == id }
            val newList = list.toMutableList()
            newList.remove(fav)
            _uiStateFavs.update { it.copy(list = newList.toList()) }
        }
    }

    fun deleteFromCart(repo: SelectedProductsRepository, id:String){
        viewModelScope.launch {
            repo.deleteFromCart(id)
            val list = _uiStateCart.value.list
            val cart = list.find { it.id == id }
            val newList = list.toMutableList()
            newList.remove(cart)
            _uiStateCart.update { it.copy(list = newList.toList()) }
        }
    }

    fun updateQuantity(repo: SelectedProductsRepository , id:String , newQuantity:Int){
        viewModelScope.launch {
            repo.updateQuantity(id, newQuantity)
            val list = _uiStateCart.value.list
            val cart = list.find { it.id == id }
            val newList = list.toMutableList()
            val idx = newList.indexOf(cart)
            newList[idx] = cart!!.copy(quantity = newQuantity)
            _uiStateCart.update { it.copy(list = newList.toList()) }
        }
    }


}