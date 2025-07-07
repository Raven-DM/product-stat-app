package com.example.productstatapp.models

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val rating: Float,
    val brand: String,
    val category: String,
    val thumbnail: String
)

data class ProductResponse(
    val products: List<Product>
)