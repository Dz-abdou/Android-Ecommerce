package com.example.eccomercek.models

import com.example.eccomercek.R

class CartModel(
    var productImage: Int,
    var productName: String,
    var productSize: String,
    var productPrice: Double,
    var productQuantity: Int
) {

    object MockList {

        fun getModel(): List<CartModel> {

            val itemModel1 = CartModel(
                R.drawable.womanshirt1,
                "Vans T-Shirt",
                "L",
                56.99,
                1
            )

            val itemModel2 = CartModel(
                R.drawable.womanshirt1,
                "Oversized Hoodie",
                "M",
                69.99,
                2
            )

            val itemModel3 = CartModel(
                R.drawable.womanshirt1,
                "V T-Shirt",
                "S",
                69.99,
                1
            )


            val itemList: ArrayList<CartModel> = ArrayList()

            for (i in 0..1) {
                itemList.add(itemModel1)
                itemList.add(itemModel2)
                itemList.add(itemModel3)

            }

            return itemList
        }

    }
}