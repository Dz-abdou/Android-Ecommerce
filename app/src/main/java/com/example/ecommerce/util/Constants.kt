package com.example.ecommerce.util

object Constants {

    const val PAGINATION_LIMIT = 15L

    const val USER_ID = "userID"

    //App
    const val TAG = "EcommerceApp"

    //Messages
    const val ERROR_MESSAGE = "Unexpected error!"

    //Database Types
    const val CLOUD_FIRESTORE = "Cloud Firestore"
    //References
    const val USERS_REF = "user"
    const val CATEGORIES_REF = "category"
    const val FAVORITES_REF = "favorites"
    const val CART_REF = "cart"
    const val SHIPPING_ADDRESSES_REF = "shippingAddresses"


    //Product references
    const val PRODUCT_NAME = "productName"
    const val PRODUCT_COLORS = "productColors"
    const val PRODUCT_IMAGES = "productImages"
    const val PRODUCT_PRICE = "productPrice"
    const val PRODUCT_SIZES = "productSizes"
    const val PRODUCT_SIZE = "productSize"
    const val PRODUCT_BRAND = "productBrand"
    const val PRODUCT_CATEGORY = "productCategory"
    const val ORDER_REF = "orders"

    //Cart references
    const val PRODUCT_QUANTITY = "productQuantity"
    const val PRODUCT_COLOR = "productColor"


    //User Fields
    const val FULLNAME = "fullName"
    const val PHONENUMBER = "phoneNumber"
    const val AGE = "age"
    const val EMAIL = "age"
    const val SEX = "sex"
    const val PRODUCTS_IDS = "productsIDs"


    //Shipping Address Fields
    const val ADDRESS = "address"
    const val CITY = "city"
    const val PROVINCE_OR_STATE_OR_REGION = "provinceOrStateOrRegion"
    const val ZIP_CODE = "zipCode"
    const val COUNTRY = "country"
    const val USE_THIS_ADDRESS = "useThisAddress"


    //User Favorite Map KEYS
    const val PRODUCTS_LIST = "productsList"
    const val PRODUCTS_CATEGORIES = "productsCategories"

    //Intent Extras KEYS
    const val EXTRA_KEY_PRODUCT = "product"
    const val EXTRA_KEY_IN_FAVORITE = "inFavorite"
    const val FROM_CART_FRAGMENT = "fromCartFragment"

    //Order Map Keys
    const val ORDER_ID = "orderID"
    const val TRACKING_CODE = "trackingCode"
    const val NUMBER_OF_PRODUCTS = "numberOfProducts"
    const val TOTAL_AMOUNT = "totalAmount"
    const val ORDER_DATE = "orderDate"
    const val DELIVERED_DATE = "deliveredDate"
    const val SHIPPING_ADDRESS = "shippingAddress"
    const val STATUS = "status"
    const val PROCESSING = "processing"
    const val DELIVERED = "delivered"
    const val CANCELED = "canceled"
    const val ORDER_PRODUCTS_REF = "products"
    const val DELIVERY_METHOD = "deliveryMethod"

    //Search Map keys
    const val CRITERIA = "criteria"
    const val SEARCH_VALUE = "searchValue"
    const val CATEGORIES = "categories"


}