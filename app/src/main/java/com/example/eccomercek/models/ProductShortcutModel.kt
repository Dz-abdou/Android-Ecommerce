package com.example.eccomercek.models

import com.example.eccomercek.R

class ProductShortcutModel(var productName: String, var productPrice: Double, var productImage: Int) {

    object MockList {

        fun getModel(): List<ProductShortcutModel> {

            val itemModel1 = ProductShortcutModel(
                "Oversized T-shirt",
                59.99,
                R.drawable.womanshirt1
            )

            val itemModel2 = ProductShortcutModel(
                "Oversized T-shirt",
                49.99,
                R.drawable.womanshirt2
            )

            val itemModel3 = ProductShortcutModel(
                "Oversized T-shirt",
                79.99,
                R.drawable.womanshirt3
            )


            val itemList: ArrayList<ProductShortcutModel> = ArrayList()

            for (i in 0..4) {
                itemList.add(itemModel1)
                itemList.add(itemModel2)
                itemList.add(itemModel3)

            }

            return itemList
        }

    }
}