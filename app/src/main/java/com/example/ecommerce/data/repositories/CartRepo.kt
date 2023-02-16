package com.example.ecommerce.data.repositories

import com.example.ecommerce.data.interfaces.CartRepoInterface
import com.example.ecommerce.data.models.OrderModel
import com.example.ecommerce.data.models.ProductInCartModel
import com.example.ecommerce.data.models.ProductModel
import com.example.ecommerce.data.models.ShippingAddressModel
import com.example.ecommerce.util.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.CompletionService
import javax.inject.Inject
import kotlin.math.log

class CartRepo @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestoreRef: FirebaseFirestore
) : CartRepoInterface {


    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
    override val firebaseFirestoreRef: FirebaseFirestore?
        get() = firestoreRef

    override suspend fun getUserCart(): Resource<List<ProductInCartModel>> {
        return try {
            val userRef = firestoreRef.collection(Constants.USERS_REF).document(currentUser?.uid!!)
            val cartResult = userRef.collection(Constants.CART_REF).get().await()
            val list = ArrayList<ProductInCartModel>()
            for (cartItem in cartResult.documents) {
                val productName = cartItem.get(Constants.PRODUCT_NAME) as String
                val productCategory = cartItem.get(Constants.PRODUCT_CATEGORY) as String
                logMessage(productCategory + productName)
                val productDocument =
                    firestoreRef.collection(productCategory.trim()).document(productName.trim())
                        .get().await()
                if (productDocument.exists()) {
                    val productID = cartItem.id
                    val productColor = cartItem.get(Constants.PRODUCT_COLOR) as String
                    val productSize = cartItem.get(Constants.PRODUCT_SIZE) as String
                    val productQuantity = cartItem.get(Constants.PRODUCT_QUANTITY) as Long
                    val productPrice = productDocument.get(Constants.PRODUCT_PRICE) as String
                    val productImages =
                        productDocument.get(Constants.PRODUCT_IMAGES) as ArrayList<String>
                    val productInCartModel = ProductInCartModel(
                        productID,
                        productImages[0],
                        productName,
                        productSize,
                        productPrice.toDouble(),
                        productQuantity.toInt(),
                        productColor,
                        productCategory
                    )
                    logMessage(productInCartModel.productImage)
                    list.add(productInCartModel)
                }
            }
            Resource.Success(list)
        } catch (e: Exception) {
            e.printStackTrace()
            logMessage(e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun removeProductFromCart(productID: String) {
        try {
            firestoreRef.collection(Constants.USERS_REF).document(currentUser?.uid!!)
                .collection(Constants.CART_REF).document(productID.trim()).delete()
                .addOnSuccessListener {
                    logMessage(productID)
                }.addOnFailureListener(OnFailureListener {
                logMessage(it.message.toString())
                it.printStackTrace()
            })
        } catch (e: Exception) {
            e.printStackTrace()
            logMessage(e.message.toString())
        }
    }

    override suspend fun addOrder(cartList: ArrayList<ProductInCartModel>, shippingAddress: ShippingAddressModel): Resource<Boolean> {
        return try {
            val map = hashMapOf<String, Any?>()
            map[Constants.USER_ID] = currentUser?.uid
            map[Constants.TRACKING_CODE] = null
            var numberOfProducts = 0
            var total = 0.0
            for (product in cartList) {
                total += product.productPrice * product.productQuantity
                numberOfProducts += product.productQuantity
            }
            map[Constants.NUMBER_OF_PRODUCTS] = numberOfProducts
            total += 20
            map[Constants.TOTAL_AMOUNT] = String.format("%.2f", total)
            map[Constants.ORDER_DATE] = FieldValue.serverTimestamp()
            map[Constants.DELIVERED_DATE] = null
            map[Constants.SHIPPING_ADDRESS] = shippingAddress.address + ", " + shippingAddress.zipCode +
                    ", " + shippingAddress.city + ", " + shippingAddress.provinceOrStateOrRegion +
                    ", " + shippingAddress.country
            map[Constants.STATUS] = Constants.PROCESSING
            val orderID = currentUser?.uid.hashCode().toString() + "-" + getTimeInMillis()
            val orderRef = firestoreRef.collection(Constants.ORDER_REF).document(orderID)
            orderRef.set(map).await()
            val orderMap = ArrayList<Map<String, Any>>()
            var counter = 1
            for (product in cartList) {
                val productMap = hashMapOf<String, Any>()
                productMap[Constants.PRODUCT_NAME] = product.productName
                productMap[Constants.PRODUCT_QUANTITY] = product.productQuantity
                productMap[Constants.PRODUCT_SIZE] = product.productSize
                productMap[Constants.PRODUCT_COLOR] = product.productColor
                productMap[Constants.PRODUCT_CATEGORY] = product.productCategory
                orderMap.add(productMap)
                orderRef.collection(Constants.ORDER_PRODUCTS_REF).document(product.productName + counter).set(productMap).await()
                counter++
            }
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            logMessage(e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun emptyCart() {
        try {
            val batchSize = 15L
            var deleted = 0
            val result = firestoreRef.collection(Constants.USERS_REF).document(currentUser?.uid!!).collection(Constants.CART_REF)
                .limit(batchSize)
                .get()
                .await()
            for (document in result.documents) {
                document.reference.delete()
                ++deleted
            }
            if (deleted >= batchSize) {
                // retrieve and delete another batch
                emptyCart()
            }
        } catch (e: Exception) {
            System.err.println("Error deleting collection : " + e.message)
        }
    }


    override suspend fun getUserSelectedShippingAddress(): Resource<ShippingAddressModel?> {
        return try {
            val result = firestoreRef.collection(Constants.USERS_REF).document(currentUser?.uid!!)
                .collection(Constants.SHIPPING_ADDRESSES_REF)
                .whereEqualTo(Constants.USE_THIS_ADDRESS, true).get().await()
            if (result.isEmpty)
                Resource.Success(null)
            else {
                lateinit var shippingAddress: ShippingAddressModel
                for (document in result.documents) {
                    val city = document.get(Constants.CITY) as String
                    val address = document.get(Constants.ADDRESS) as String
                    val zipCode = document.get(Constants.ZIP_CODE) as String
                    val province = document.get(Constants.PROVINCE_OR_STATE_OR_REGION) as String
                    val country = document.get(Constants.COUNTRY) as String
                    shippingAddress =
                        ShippingAddressModel(address, city, province, zipCode, country, true)
                }
                Resource.Success(shippingAddress)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            logMessage(e.message.toString())
            Resource.Failure(e)
        }
    }
}