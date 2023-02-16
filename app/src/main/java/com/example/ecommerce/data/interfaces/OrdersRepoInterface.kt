package com.example.ecommerce.data.interfaces

import com.example.ecommerce.data.models.OrderModel
import com.example.ecommerce.data.models.ProductInCartModel
import com.example.ecommerce.data.repositories.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

interface OrdersRepoInterface {

    val currentUser: FirebaseUser?
    val firebaseFirestoreRef: FirebaseFirestore?
    suspend fun cancelOrder(orderID: String)
    suspend fun getAllOrders(): Resource<Map<String, List<OrderModel>>>
    suspend fun getOrderDetails(orderID: String): Resource<List<ProductInCartModel>>
}