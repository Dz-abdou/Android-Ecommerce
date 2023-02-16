package com.example.ecommerce.data.interfaces

import com.example.ecommerce.data.repositories.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

interface ProfileRepoInterface {
    fun logout()
}