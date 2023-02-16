package com.example.ecommerce.data.interfaces

import com.example.ecommerce.data.repositories.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

interface AuthRepoInterface {
    val currentUser: FirebaseUser?
    val firebaseFirestoreRef: FirebaseFirestore?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String): Resource<FirebaseUser>
    suspend fun createUserInFirestore(name: String, age: Number, sex: String, email: String): Resource<Any>
    fun logout()
}