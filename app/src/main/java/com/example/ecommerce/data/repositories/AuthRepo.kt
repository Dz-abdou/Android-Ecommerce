package com.example.ecommerce.data.repositories

import com.example.ecommerce.data.interfaces.AuthRepoInterface
import com.example.ecommerce.data.models.UserModel
import com.example.ecommerce.util.Constants
import com.example.ecommerce.util.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Objects

import javax.inject.Inject

class AuthRepo @Inject constructor(private val firebaseAuth: FirebaseAuth, private val firestoreRef: FirebaseFirestore) :
    AuthRepoInterface {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override val firebaseFirestoreRef: FirebaseFirestore
        get() = firestoreRef



    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            return Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }    }

    override suspend fun createUserInFirestore(
        name: String,
        age: Number,
        sex: String,
        email: String
    ): Resource<Any> {
        return try {
            val user = UserModel(name, age, sex, email)
            firebaseFirestoreRef.collection(Constants.USERS_REF).document(currentUser?.uid!!).set(user).await()
            return Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }

    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}