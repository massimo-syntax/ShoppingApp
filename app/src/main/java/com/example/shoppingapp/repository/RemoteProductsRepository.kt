package com.example.shoppingapp.repository

import com.example.shoppingapp.api.RetrofitInstance
import com.example.shoppingapp.data.remote.Product

class RemoteProductsRepository {
    private val productsService = RetrofitInstance.productsService

    suspend fun getProducts(): List<Product> {
        return productsService.getProducts()
    }
}