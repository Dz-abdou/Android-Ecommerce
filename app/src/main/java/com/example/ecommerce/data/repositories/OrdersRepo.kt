package com.example.ecommerce.data.repositories

import com.example.ecommerce.data.interfaces.OrdersRepoInterface
import com.example.ecommerce.data.models.OrderModel
import com.example.ecommerce.data.models.ProductInCartModel
import com.example.ecommerce.util.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.log

class OrdersRepo @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestoreRef: FirebaseFirestore
) :
    OrdersRepoInterface {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
    override val firebaseFirestoreRef: FirebaseFirestore
        get() = firestoreRef


    override suspend fun getAllOrders(): Resource<Map<String, List<OrderModel>>> {
        return try {
            val result = firestoreRef.collection(Constants.ORDER_REF)
                .whereEqualTo(Constants.USER_ID, currentUser?.uid).get().await()
            val processing = ArrayList<OrderModel>()
            val delivered = ArrayList<OrderModel>()
            for (document in result.documents) {
                val orderID = document.id
                val trackingCode = document.get(Constants.TRACKING_CODE) as String?
                val numberOfProducts = document.get(Constants.NUMBER_OF_PRODUCTS)
                var totalAmount = document.get(Constants.TOTAL_AMOUNT) as String
                val totalAmountDouble = totalAmount.toDouble()
                totalAmount = String.format("%.2f", totalAmountDouble)
                val orderDate = document.get(Constants.ORDER_DATE) as Timestamp
                val deliveredDate = document.get(Constants.DELIVERED_DATE) as Timestamp?
                val shippingAddress = document.get(Constants.SHIPPING_ADDRESS) as String
                val status = document.get(Constants.STATUS) as String
                val deliveryMethod = document.get(Constants.DELIVERY_METHOD) as String?
                val orderModel = OrderModel(
                    orderID,
                    trackingCode,
                    numberOfProducts.toString().toInt(),
                    totalAmount.toDouble(),
                    orderDate,
                    deliveryMethod,
                    deliveredDate,
                    shippingAddress,
                    status
                )
                if (status == Constants.PROCESSING)
                    processing.add(orderModel)
                else
                    delivered.add(orderModel)
            }
            val map = hashMapOf<String, List<OrderModel>>()
            processing.reverse()
            delivered.reverse()
            map[Constants.PROCESSING] = processing
            map[Constants.DELIVERED] = delivered
            Resource.Success(map)
        } catch (e: Exception) {
            e.printStackTrace()
            logMessage(e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun getOrderDetails(orderID: String): Resource<List<ProductInCartModel>> {
        return try {
            val result = firestoreRef.collection(Constants.ORDER_REF).document(orderID)
                .collection(Constants.ORDER_PRODUCTS_REF)
                .get().await()
            val products = ArrayList<ProductInCartModel>()
            for (product in result.documents) {
                val id = product.id
                val productName = product.get(Constants.PRODUCT_NAME) as String
                val productCategory = product.get(Constants.PRODUCT_CATEGORY) as String
                val productColor = product.get(Constants.PRODUCT_COLOR) as String
                val productSize = product.get(Constants.PRODUCT_SIZE) as String
                val numberOfProducts = product.get(Constants.PRODUCT_QUANTITY)

                val productRef =
                    firestoreRef.collection(productCategory).document(productName).get().await()
                val productImages = productRef.get(Constants.PRODUCT_IMAGES) as ArrayList<String>
                val productPrice = productRef.get(Constants.PRODUCT_PRICE) as String
                val productImage = productImages[0]

                products.add(
                    ProductInCartModel(
                        id,
                        productImage,
                        productName,
                        productSize,
                        productPrice.toDouble(),
                        numberOfProducts.toString().toInt(),
                        productColor,
                        productCategory
                    )
                )
            }

            Resource.Success(products)
        } catch (e: Exception) {
            e.printStackTrace()
            logMessage(e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun cancelOrder(orderID: String) {
        TODO("Not yet implemented")
    }
}