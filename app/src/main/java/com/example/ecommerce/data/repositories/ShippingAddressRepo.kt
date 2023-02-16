package com.example.ecommerce.data.repositories

import com.example.ecommerce.data.interfaces.ShippingAddressesRepoInterface
import com.example.ecommerce.data.models.ShippingAddressModel
import com.example.ecommerce.util.Constants
import com.example.ecommerce.util.await
import com.example.ecommerce.util.logMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ShippingAddressRepo @Inject constructor(private val firebaseAuth: FirebaseAuth, private val firestoreRef: FirebaseFirestore) :
    ShippingAddressesRepoInterface {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
    override val fireStoreRef: FirebaseFirestore
        get() = firestoreRef

    override suspend fun addUserShippingAddress(shippingAddressModel: ShippingAddressModel) {
        try {
            fireStoreRef.collection(Constants.USERS_REF).document(currentUser?.uid!!).collection(Constants.SHIPPING_ADDRESSES_REF)
                .document(shippingAddressModel.address+ " " +shippingAddressModel.city).set(shippingAddressModel).await()
        } catch (e: Exception) {
            e.printStackTrace()
            logMessage(e.message.toString())
        }
    }

    override suspend fun removeShippingAddress(shippingAddressID: String) {
        try {
            fireStoreRef.collection(Constants.USERS_REF).document(currentUser?.uid!!).collection(Constants.SHIPPING_ADDRESSES_REF)
                .document(shippingAddressID).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
            logMessage(e.message.toString())
        }
    }

    override suspend fun getShippingAddresses(): Resource<List<ShippingAddressModel>> {
        return try {
            val result = fireStoreRef.collection(Constants.USERS_REF).document(currentUser?.uid!!)
                .collection(Constants.SHIPPING_ADDRESSES_REF).get().await()

            val list = ArrayList<ShippingAddressModel>()

            for (document in result.documents) {
                val address = document.get(Constants.ADDRESS) as String
                val city = document.get(Constants.CITY) as String
                val provinceOrStateOrRegion = document.get(Constants.PROVINCE_OR_STATE_OR_REGION) as String
                val zipCode = document.get(Constants.ZIP_CODE) as String
                val country = document.get(Constants.COUNTRY) as String
                val useThisAddress = document.get(Constants.USE_THIS_ADDRESS) as Boolean

                list.add(ShippingAddressModel(address, city, provinceOrStateOrRegion, zipCode, country, useThisAddress))
            }

            Resource.Success(list)
        } catch (e: Exception) {
            e.printStackTrace()
            logMessage(e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun removeAddressUsed(shippingAddressID: String) {
        try {
            fireStoreRef.collection(Constants.USERS_REF).document(currentUser?.uid!!).collection(Constants.SHIPPING_ADDRESSES_REF)
                .document(shippingAddressID).update(Constants.USE_THIS_ADDRESS, false).await()
        } catch (e: Exception) {
            e.printStackTrace()
            logMessage(e.message.toString())
        }
    }

    override suspend fun addAddressUsed(shippingAddressID: String) {
        try {
            fireStoreRef.collection(Constants.USERS_REF).document(currentUser?.uid!!).collection(Constants.SHIPPING_ADDRESSES_REF)
                .document(shippingAddressID).update(Constants.USE_THIS_ADDRESS, true).await()
        } catch (e: Exception) {
            e.printStackTrace()
            logMessage(e.message.toString())
        }
    }
}