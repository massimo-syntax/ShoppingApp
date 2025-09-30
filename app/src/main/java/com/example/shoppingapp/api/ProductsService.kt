package com.example.shoppingapp.api

import com.example.shoppingapp.data.remote.Product
import retrofit2.http.GET

interface ProductsService {
    //https://fakestoreapi.com/products
    @GET("products")
    suspend fun getProducts(): List<Product>
}