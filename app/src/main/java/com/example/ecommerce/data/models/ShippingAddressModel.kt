package com.example.ecommerce.data.models

class ShippingAddressModel(
    val address: String,
    val city: String,
    val provinceOrStateOrRegion: String,
    val zipCode: String,
    val country: String,
    var useThisAddress: Boolean
):java.io.Serializable {

}