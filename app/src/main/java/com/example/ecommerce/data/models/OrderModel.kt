package com.example.ecommerce.data.models

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

class OrderModel(
    val orderID: String,
    val trackingCode: String?,
    val numberOfProducts: Int,
    val totalAmount: Double,
    val orderDate: Timestamp,
    val deliveryMethod: String?,
    val deliveredDate: Timestamp?,
    val shippingAddress: String,
    val status: String
    ) {




}
