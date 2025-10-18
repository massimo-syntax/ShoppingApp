package com.example.shoppingapp.api

import com.example.shoppingapp.data.remote.RemoteProduct
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductsService {
    //https://fakestoreapi.com/products
    @GET("products")
    suspend fun getProducts(): List<RemoteProduct>

    //https://fakestoreapi.com/products/id
    @GET("products/{id}")
    suspend fun getSingleProduct(@Path("id") id: String): RemoteProduct
}