package com.example.productanalyticsapp.network


import com.example.productanalyticsapp.model.Product
import com.example.productanalyticsapp.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("products")
    suspend fun getAllProducts(): Response<ProductResponse>

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String
    ): Response<ProductResponse>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") productId: Int
    ): Response<Product>
}
