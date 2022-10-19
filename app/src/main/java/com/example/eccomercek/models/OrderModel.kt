package com.example.eccomercek.models

import com.example.eccomercek.R
import java.text.SimpleDateFormat
import java.util.Date

class OrderModel(
    val orderNumber: Int,
    val orderDate: String,
    val trackingCode: String,
    val quantity: Int,
    val totalAmount: Double
    ) {



    object MockList {

        private val sdf = SimpleDateFormat("dd/mm/yyyy")
        val currentDate = sdf.format(Date())

        fun getModel(): List<OrderModel> {

            val itemModel1 = OrderModel(
                23599715,
                currentDate,
                "YA5988745325",
                3,
                192.99,
            )

            val itemModel2 = OrderModel(
                23599715,
                currentDate,
                "YA5988745325",
                3,
                192.99,
            )

            val itemModel3 = OrderModel(
                23599715,
                currentDate,
                "YA5988745325",
                3,
                192.99,
            )

            val itemModel4 = OrderModel(
                23599715,
                currentDate,
                "YA5988745325",
                3,
                192.99,
            )


            val itemList: ArrayList<OrderModel> = ArrayList()

            itemList.add(itemModel1)
            itemList.add(itemModel2)
            itemList.add(itemModel3)
            itemList.add(itemModel4)

            return itemList
        }

    }


}
