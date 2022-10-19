package com.example.eccomercek.models

class ShippingAddressModel(
    val recieverName: String,
    val address: String,
    val city: String,
    val provinceOrStateOrRegion: String,
    val zipCode: String,
    val country: String,
    val useCheckBox: Boolean = false
) {


    object MockList {

        fun getModel(): List<ShippingAddressModel> {

            val itemModel1 = ShippingAddressModel(
                "Abdenour Bacha",
                "ICS plaine ouest B:b N:9",
                "Annaba",
                "Annaba",
                "23000",
                "Algeria"
            )

            val itemModel2 = ShippingAddressModel(
                "Abdenour Bacha",
                "ICS plaine ouest B:b N:9",
                "Annaba",
                "Annaba",
                "23000",
                "Algeria"
            )

            val itemModel3 = ShippingAddressModel(
                "Abdenour Bacha",
                "ICS plaine ouest B:b N:9",
                "Annaba",
                "Annaba",
                "23000",
                "Algeria"
            )


            val itemList: ArrayList<ShippingAddressModel> = ArrayList()

            itemList.add(itemModel1)
            itemList.add(itemModel2)
            itemList.add(itemModel3)


            return itemList
        }

    }
}