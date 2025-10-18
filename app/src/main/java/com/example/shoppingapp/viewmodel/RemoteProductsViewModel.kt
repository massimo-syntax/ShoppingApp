package com.example.shoppingapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.model.UiProductWithFieldsFromRoom
import com.example.shoppingapp.data.remote.RemoteProduct
import com.example.shoppingapp.repository.RemoteProductsRepository
import com.example.shoppingapp.repository.SelectedProductsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RemoteProductsViewModel() : ViewModel() {
    // Repository to query JSON API
    private val repository = RemoteProductsRepository()

    // UI-Product is because the product from JSON does not have cart or fav
    // cart and fav are from room stored locally on device
    // this class is used to update the ui, also to have is fav or is in cart property
    val uiProducts = mutableStateOf<List<UiProductWithFieldsFromRoom>>(emptyList())

    fun fetchRemoteProducts(roomRepository: SelectedProductsRepository) {
        viewModelScope.launch {
            try {
                // get products from JSON API
                val productsResponse = repository.getProducts()

                // get list of cart and favourites
                val cart = roomRepository.getCart().map { it.productId }
                val favs = roomRepository.getFavs().map { it.productId }

                // create fill a list with product objects capable of cart-fav + toggle
                val ui_Products = mutableListOf<UiProductWithFieldsFromRoom>()
                productsResponse.forEachIndexed { index, it ->
                    val ui_p = UiProductWithFieldsFromRoom(
                        it.id,
                        it.title,
                        it.description,
                        it.image,
                        it.price,
                        cart.contains(it.id),
                        favs.contains(it.id)
                    )
                    ui_Products.add(ui_p)
                }

                // post data for recomposition
                uiProducts.value = ui_Products

            } catch (e: Exception) {
                Log.wtf("NO DATA FROM REMOTE , VIEWMODEL", "-- ERROR IN VIEWMODEL ::: ${e.message}")
                //products.value = emptyList()
            }
        }
    }


}