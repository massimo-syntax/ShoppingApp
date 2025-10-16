package com.example.shoppingapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.remote.Product
import com.example.shoppingapp.repository.RemoteProductsRepository
import com.example.shoppingapp.screen.UiProduct
import com.google.api.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RemoteProductsViewModel() : ViewModel() {
    private val repository = RemoteProductsRepository()
    val products = mutableStateOf<List<Product>>(emptyList())

    val uiProducts = mutableStateOf<List<UiProduct>>(emptyList())

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                Log.wtf("MANY DATA FROM REMOTE , VIEWMODEL", "-- DATA FROM VIEWMODEL.. WAITING DATA")
                Log.wtf("MANY DATA FROM REMOTE , VIEWMODEL", "-- DATA FROM VIEWMODEL.. WAITING DATA")
                Log.wtf("MANY DATA FROM REMOTE , VIEWMODEL", "-- DATA FROM VIEWMODEL.. WAITING DATA")
                Log.wtf("MANY DATA FROM REMOTE , VIEWMODEL", "-- DATA FROM VIEWMODEL.. WAITING DATA")
                Log.wtf("MANY DATA FROM REMOTE , VIEWMODEL", "-- DATA FROM VIEWMODEL.. WAITING DATA")
                Log.wtf("MANY DATA FROM REMOTE , VIEWMODEL", "-- DATA FROM VIEWMODEL.. WAITING DATA")
                Log.wtf("MANY DATA FROM REMOTE , VIEWMODEL", "-- DATA FROM VIEWMODEL.. WAITING DATA")
                Log.wtf("MANY DATA FROM REMOTE , VIEWMODEL", "-- DATA FROM VIEWMODEL.. WAITING DATA")

                val productsResponse = repository.getProducts()
                val cart = getCart()

                val ui_Products = mutableListOf<UiProduct>()

                productsResponse.forEachIndexed { index, it ->
                    val ui_p = UiProduct(
                        it.id,
                        it.title,
                        it.image,
                        it.price,
                        cart.contains(index)
                    )
                    ui_Products.add(ui_p)
                }
                products.value = productsResponse
                uiProducts.value = ui_Products

            } catch (e: Exception) {
                products.value = emptyList()
                Log.wtf("NO DATA FROM REMOTE , VIEWMODEL", "-- ERROR IN VIEWMODEL ::: ${e.message}")
            }
        }
    }

    suspend fun getCart():List<Int> {
        delay(2000)
        return listOf(0,3,5)
    }

}