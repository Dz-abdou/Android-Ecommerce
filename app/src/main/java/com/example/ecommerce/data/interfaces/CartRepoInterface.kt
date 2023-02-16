package com.example.ecommerce.data.interfaces

import com.example.ecommerce.data.models.OrderModel
import com.example.ecommerce.data.models.ProductInCartModel
import com.example.ecommerce.data.models.ShippingAddressModel
import com.example.ecommerce.data.repositories.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

interface CartRepoInterface {
    val currentUser: FirebaseUser?
    val firebaseFirestoreRef: FirebaseFirestore?
    suspend fun getUserCart(): Resource<List<ProductInCartModel>>
    suspend fun removeProductFromCart(productID: String)
    suspend fun getUserSelectedShippingAddress() : Resource<ShippingAddressModel?>
    suspend fun addOrder(cartList: ArrayList<ProductInCartModel>, shippingAddress: ShippingAddressModel): Resource<Boolean>
    suspend fun emptyCart()
}