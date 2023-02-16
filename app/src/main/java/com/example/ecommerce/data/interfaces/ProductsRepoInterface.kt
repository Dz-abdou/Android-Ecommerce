package com.example.ecommerce.data.interfaces

import com.example.ecommerce.data.models.ProductInCartModel
import com.example.ecommerce.data.models.ProductModel
import com.example.ecommerce.data.repositories.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

interface ProductsRepoInterface {

    val currentUser: FirebaseUser?
    val firebaseFirestoreRef: FirebaseFirestore?

    suspend fun getCategories(): Resource<List<String>>
    suspend fun getProductsByCategory(category: String): Resource<List<ProductModel>>?
    suspend fun getProductData(productID: String): Resource<DocumentSnapshot>
    suspend fun getUserFavorites(): Resource<ArrayList<ProductModel>>
    suspend fun getUserFavoritesIDsByCategory(category: String): Resource<List<String>>
    suspend fun addProductToFavorites(productID: String, category: String)
    suspend fun removeProductFromFavorites(productID: String, category: String)
    suspend fun addProductToCart(product: ProductInCartModel)
    suspend fun searchProduct(criteria: String, searchString: String, categories: ArrayList<String>): Resource<ArrayList<ProductModel>>
}