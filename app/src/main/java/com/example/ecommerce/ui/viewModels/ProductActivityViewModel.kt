package com.example.ecommerce.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.models.ProductInCartModel
import com.example.ecommerce.data.models.ProductModel
import com.example.ecommerce.data.repositories.ProductsRepo
import com.example.ecommerce.data.repositories.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductActivityViewModel @Inject constructor(private val productsRepo: ProductsRepo): ViewModel() {

    private var _productInFavoritesLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var productInFavoritesLiveData: LiveData<Boolean> = _productInFavoritesLiveData


    fun addProductToFavorites(productID: String, category: String) {
        viewModelScope.launch {
            productsRepo.addProductToFavorites(productID, category)
        }
    }

    fun removeProductFromFavorite(productID: String, category: String) {
        viewModelScope.launch {
            productsRepo.removeProductFromFavorites(productID, category)
        }
    }

    fun setProductInFavoritesLiveData(productInFavorites: Boolean) {
        _productInFavoritesLiveData.value = productInFavorites
    }

    fun addProductToCart(product: ProductInCartModel) {
        viewModelScope.launch {
            productsRepo.addProductToCart(product)
        }
    }


}