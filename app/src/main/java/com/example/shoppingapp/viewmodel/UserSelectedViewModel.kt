package com.example.shoppingapp.viewmodel

import android.util.Log
import androidx.core.text.isDigitsOnly
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

    private val _uiStateCart = MutableStateFlow(UIState())
    val uiStateCart: StateFlow<UIState> = _uiStateCart.asStateFlow()

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

        val cartIds = roomRepo.getCart().map { it.productId }
        val jsonApiIds = mutableListOf<String>()
        val firebaseIds = mutableListOf<String>()
        val jsonApiCart = mutableListOf<UserSelectedProduct>()
        val completeList = mutableListOf<UserSelectedProduct>()

        cartIds.forEach {
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
        _uiStateCart.update { it.copy(roomDataLoaded = true, list = completeList.toList()) }

        var count = 0
        val firebasePproducts = mutableListOf<UserSelectedProduct>()

        // also when there is no
        if (firebaseIds.isEmpty()){
            _uiStateCart.update { it.copy(firebaseDataLoaded = true) }
            return
        }

        firebaseIds.forEach { id ->
            Firebase.firestore
                .collection("products")
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (document.data == null) return@addOnSuccessListener
                    val product = documentToSelectedProduct(document.data!!)
                    firebasePproducts.add(product)
                    count++
                    if (count == firebaseIds.size) {
                        completeList.addAll(firebasePproducts)
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


}