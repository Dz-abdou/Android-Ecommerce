package com.example.ecommerce.data.repositories

import com.example.ecommerce.data.interfaces.AuthRepoInterface
import com.example.ecommerce.data.interfaces.ProfileRepoInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class ProfileRepo @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    ProfileRepoInterface {

    override fun logout() {
        firebaseAuth.signOut()
    }
}