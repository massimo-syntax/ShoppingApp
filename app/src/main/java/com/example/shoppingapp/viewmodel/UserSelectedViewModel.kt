package com.example.shoppingapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingapp.data.model.UiProductWithFieldsFromRoom
import com.example.shoppingapp.data.model.UserSelectedProduct
import com.example.shoppingapp.data.remote.RemoteProduct
import com.example.shoppingapp.features.UIState
import com.example.shoppingapp.repository.RemoteProductsRepository
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserSelectedViewModel() : ViewModel() {

    data class UIState(
        val roomDataLoaded: Boolean = false,
        val firebaseDataLoaded: Boolean = false,
        val list: List<UserSelectedProduct> = emptyList()
    )

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    private val remoteRepo = RemoteProductsRepository()
    private val productsViewModel = ProductsViewModel()


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

        val cartIds = roomRepo.getCart().map { it.productId }
        val _jsonApiIds = mutableListOf<String>()
        val _firebaseIds = mutableListOf<String>()
        val _jsonApiCart = mutableListOf<UserSelectedProduct>()
        val completeList = mutableListOf<UserSelectedProduct>()

        cartIds.forEach {
            if (it.length > 1) { // product is stored in fierbase
                _firebaseIds.add(it)
            } else { // product is from json api
                _jsonApiIds.add(it)
                val jsonProductRemote: RemoteProduct = remoteRepo.getProduct(it)
                val jsonProduct = UserSelectedProduct(
                    id = jsonProductRemote.id,
                    title = jsonProductRemote.title,
                    description = jsonProductRemote.description,
                    mainPicture = jsonProductRemote.image,
                    price = jsonProductRemote.price,
                )
                _jsonApiCart.add(jsonProduct)
            }
        }
        completeList.addAll(_jsonApiCart)
        _uiState.update { it.copy(roomDataLoaded = true , list = completeList.toList()) }

        var count = 0
        val products = mutableListOf<UserSelectedProduct>()

        _firebaseIds.forEach { id ->
            Firebase.firestore
                .collection("products")
                .document(id)
                .get()
                .addOnSuccessListener { document ->

                    val product = documentToSelectedProduct(document.data!!)
                    products.add(product)
                    count++
                    if (count == _firebaseIds.size) {
                        Log.wtf("count==id.size", "$count - ${_firebaseIds.size}")

                        Log.wtf("count==id.size", "$count - ${_firebaseIds.size}")
                        Log.wtf("count==id.size", "$count - ${_firebaseIds.size}")
                        Log.wtf("count==id.size", "$count - ${_firebaseIds.size}")

                        completeList.addAll(products)
                        _uiState.update { it.copy(firebaseDataLoaded = true , list = completeList.toList()) }
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

}