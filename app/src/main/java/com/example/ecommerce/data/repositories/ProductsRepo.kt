package com.example.ecommerce.data.repositories

import android.location.Criteria
import com.example.ecommerce.data.interfaces.ProductsRepoInterface
import com.example.ecommerce.data.models.ProductInCartModel
import com.example.ecommerce.data.models.ProductModel
import com.example.ecommerce.util.Constants
import com.example.ecommerce.util.await
import com.example.ecommerce.util.logMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import javax.inject.Inject

class ProductsRepo @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestoreRef: FirebaseFirestore
) :
    ProductsRepoInterface {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
    override val firebaseFirestoreRef: FirebaseFirestore
        get() = firestoreRef

    override suspend fun getCategories(): Resource<List<String>> {
        return try {
            val result = firestoreRef.collection(Constants.CATEGORIES_REF).get().await()
            val categories = ArrayList<String>()
            for (i in result.documents) {
                categories.add(i.id)
            }
            Resource.Success(categories)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun getProductsByCategory(category: String): Resource<List<ProductModel>> {
        return try {
            val result = firestoreRef.collection(category).get().await()
            val productsList = ArrayList<ProductModel>()
            for (i in result.documents) {
                val productName = i.get(Constants.PRODUCT_NAME) as String
                val productPrice = i.get(Constants.PRODUCT_PRICE) as String
                val sizes = i.get(Constants.PRODUCT_SIZES) as List<String>
                val productImages = i.get(Constants.PRODUCT_IMAGES) as List<String>
                val productColors = i.get(Constants.PRODUCT_COLORS) as List<String>
                val productBrand = i.get(Constants.PRODUCT_BRAND) as String

                val product = ProductModel(
                    productImages,
                    productName,
                    productPrice.toDouble(),
                    sizes,
                    productColors,
                    category,
                    productBrand
                )
                productsList.add(product)
            }
            Resource.Success(productsList)
        } catch (e: Exception) {
            Resource.Failure(e)
        }

    }


    override suspend fun getProductData(productID: String): Resource<DocumentSnapshot> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserFavorites(): Resource<ArrayList<ProductModel>> {
        return try {
            val favoritesCollection = currentUser?.let {
                firestoreRef.collection(Constants.USERS_REF).document(it.uid)
                    .collection(Constants.FAVORITES_REF).get().await()
            }

            val productsList = ArrayList<ProductModel>()

            for (i in favoritesCollection?.documents!!) {
                val category = i.id
                val productsIDs = i.get(Constants.PRODUCTS_IDS) as ArrayList<String>?
                if (productsIDs!!.isNotEmpty()) {
                    for (productID in productsIDs) {
                        val result =
                            firestoreRef.collection(category).document(productID).get().await()
                        if (result.exists()) {
                            val productName = result.get(Constants.PRODUCT_NAME) as String
                            val productPrice = result.get(Constants.PRODUCT_PRICE) as String
                            val sizes = result.get(Constants.PRODUCT_SIZES) as List<String>
                            val productImages = result.get(Constants.PRODUCT_IMAGES) as List<String>
                            val productColors = result.get(Constants.PRODUCT_COLORS) as List<String>
                            val productBrand = result.get(Constants.PRODUCT_BRAND) as String
                            val product = ProductModel(
                                productImages,
                                productName,
                                productPrice.toDouble(),
                                sizes,
                                productColors,
                                category,
                                productBrand
                            )
                            productsList.add(product)
                        }
                    }
                }
            }
            Resource.Success(productsList)
        } catch (e: Exception) {
            logMessage(e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun getUserFavoritesIDsByCategory(category: String): Resource<ArrayList<String>> {
        return try {
            val ref = currentUser?.let {
                firestoreRef.collection(Constants.USERS_REF).document(it.uid)
                    .collection(Constants.FAVORITES_REF).document(category).get().await()
            }
            var userFavoritesIDs = ArrayList<String>()
            if (ref?.exists()!!)
                userFavoritesIDs = ref.get(Constants.PRODUCTS_IDS) as ArrayList<String>

            Resource.Success(userFavoritesIDs)
        } catch (e: Exception) {
            e.printStackTrace()
            logMessage(e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun addProductToFavorites(productID: String, category: String) {
        try {
            val ref = firestoreRef.collection(Constants.USERS_REF).document(currentUser?.uid!!)
                .collection(Constants.FAVORITES_REF).document(category)

            if (ref.get().await().exists())
                ref.update(Constants.PRODUCTS_IDS, FieldValue.arrayUnion(productID))
            else {
                val map = hashMapOf<String, ArrayList<String>>()
                val arrayList = ArrayList<String>()
                arrayList.add(productID)
                map[Constants.PRODUCTS_IDS] = arrayList
                ref.set(map)
            }
        } catch (e: Exception) {
            logMessage(e.message.toString())
            e.printStackTrace()
        }
    }

    override suspend fun removeProductFromFavorites(productID: String, category: String) {
        try {
            val ref = firestoreRef.collection(Constants.USERS_REF).document(currentUser?.uid!!)
                .collection(Constants.FAVORITES_REF).document(category)

            if (ref.get().await().exists())
                ref.update(Constants.PRODUCTS_IDS, FieldValue.arrayRemove(productID))
        } catch (e: Exception) {
            logMessage(e.message.toString())
            e.printStackTrace()
        }
    }

    override suspend fun addProductToCart(product: ProductInCartModel) {
        try {
            val map = hashMapOf<String, Any>()
            map[Constants.PRODUCT_NAME] = product.productName
            map[Constants.PRODUCT_QUANTITY] = product.productQuantity
            map[Constants.PRODUCT_SIZE] = product.productSize
            map[Constants.PRODUCT_COLOR] = product.productColor
            map[Constants.PRODUCT_CATEGORY] = product.productCategory
            firestoreRef.collection(Constants.USERS_REF).document(currentUser?.uid!!)
                .collection(Constants.CART_REF).document(product.productID).set(map)
        } catch (e: Exception) {
            e.printStackTrace()
            logMessage(e.message.toString())
        }
    }

    override suspend fun searchProduct(
        criteria: String,
        searchString: String,
        categories: ArrayList<String>
    ): Resource<ArrayList<ProductModel>> {
        return try {
            val products: ArrayList<ProductModel> = ArrayList();
            for (category in categories) {
                var query: Query = firestoreRef.collection(category)
                query = query.whereEqualTo(criteria, searchString)

                val result = query.get().await()
                for (document in result.documents) {
                    val productName = document.get(Constants.PRODUCT_NAME) as String
                    val productPrice = document.get(Constants.PRODUCT_PRICE) as String
                    val sizes = document.get(Constants.PRODUCT_SIZES) as List<String>
                    val productImages = document.get(Constants.PRODUCT_IMAGES) as List<String>
                    val productColors = document.get(Constants.PRODUCT_COLORS) as List<String>
                    val productBrand = document.get(Constants.PRODUCT_BRAND) as String
                    val product = ProductModel(
                        productImages,
                        productName,
                        productPrice.toDouble(),
                        sizes,
                        productColors,
                        category,
                        productBrand
                    )
                    products.add(product)
                }
            }
            Resource.Success(products)
        } catch (e: Exception) {
            logMessage(e.message.toString())
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}