package com.example.ecommerce.data.models


data class ProductModel(
    val productImages: List<String>,
    val productName: String,
    val productPrice: Double,
    val productSizes: List<String>,
    val productColors: List<String>,
    val productCategory: String,
    val productBrand: String
): java.io.Serializable {



}