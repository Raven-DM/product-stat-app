package com.example.productstatapp.network

import com.example.productstatapp.models.Category
import com.example.productstatapp.models.Product
import com.example.productstatapp.models.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("products")
    suspend fun getProducts(): Response<ProductResponse>

    @GET("products/search")
    suspend fun searchProduct(@Query("q") query: String): Response<ProductResponse>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>

    @GET("products/categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("products/category/{slug}")
    suspend fun getProductsByCategory(@Path("slug") slug: String): Response<ProductResponse>

}


