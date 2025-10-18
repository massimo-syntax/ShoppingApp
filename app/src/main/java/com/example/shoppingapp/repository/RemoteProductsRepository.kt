package com.example.shoppingapp.repository

import com.example.shoppingapp.api.RetrofitInstance
import com.example.shoppingapp.data.remote.RemoteProduct

class RemoteProductsRepository {
    private val productsService = RetrofitInstance.productsService

    suspend fun getProducts(): List<RemoteProduct> {
        return productsService.getProducts()
    }

    suspend fun getProduct(id:String): RemoteProduct{
        return productsService.getSingleProduct(id)
    }
}