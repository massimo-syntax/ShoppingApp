package com.example.shoppingapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.remote.Product
import com.example.shoppingapp.repository.RemoteProductsRepository
import kotlinx.coroutines.launch

class RemoteProductsViewModel : ViewModel() {
    private val repository = RemoteProductsRepository()
    val products = mutableStateOf<List<Product>>(emptyList())


    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val productsResponse = repository.getProducts()
                products.value = productsResponse
            } catch (e: Exception) {
                products.value = emptyList()
                Log.wtf("NO DATA FROM REMOTE , VIEWMODEL", "-- ERROR IN VIEWMODEL ::: ${e.message}")
            }
        }
    }

}