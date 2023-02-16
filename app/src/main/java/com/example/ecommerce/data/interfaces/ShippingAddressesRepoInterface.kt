package com.example.ecommerce.data.interfaces

import com.example.ecommerce.data.models.ShippingAddressModel
import com.example.ecommerce.data.repositories.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

interface ShippingAddressesRepoInterface {
    val currentUser: FirebaseUser?
    val fireStoreRef: FirebaseFirestore?
    suspend fun addUserShippingAddress(shippingAddressModel: ShippingAddressModel)
    suspend fun removeShippingAddress(shippingAddressID: String)
    suspend fun getShippingAddresses(): Resource<List<ShippingAddressModel>>
    suspend fun removeAddressUsed(shippingAddressID: String)
    suspend fun addAddressUsed(shippingAddressID: String)
}