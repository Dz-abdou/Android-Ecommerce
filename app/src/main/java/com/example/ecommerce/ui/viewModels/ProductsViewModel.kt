package com.example.ecommerce.ui.viewModels

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.models.ProductModel
import com.example.ecommerce.data.repositories.ProductsRepo
import com.example.ecommerce.data.repositories.Resource
import com.example.ecommerce.util.Constants
import com.example.ecommerce.util.logMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val productsRepo: ProductsRepo) : ViewModel() {

    private val _categoriesLiveData = MutableLiveData<Resource<List<String>>?>(null)
    val categoriesLiveData: LiveData<Resource<List<String>>?> = _categoriesLiveData

    private val _productsLiveData = MutableLiveData<Resource<List<ProductModel>>?>(null)
    val productsLiveData: LiveData<Resource<List<ProductModel>>?> = _productsLiveData

    private val _userFavoritesIDsLiveData = MutableLiveData<Resource<ArrayList<String>>?>(null)
    val userFavoritesIDsLiveData: LiveData<Resource<ArrayList<String>>?> = _userFavoritesIDsLiveData

    private val _userFavoritesLiveData = MutableLiveData<Resource<ArrayList<ProductModel>>?>(null)
    val userFavoritesLiveData: LiveData<Resource<ArrayList<ProductModel>>?> = _userFavoritesLiveData

    private val _selectedCategory = MutableLiveData<String>(null)
    val selectedCategory: LiveData<String> = _selectedCategory

    var searchQuery = hashMapOf<String, Any>()




    init {
        _selectedCategory.value = ""
        viewModelScope.launch {
            getCategories()
            getUserFavorites()
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            _categoriesLiveData.value = Resource.Loading
            val categoryResult = productsRepo.getCategories()
            _categoriesLiveData.value = categoryResult
        }
    }

    fun getProductsByCategory(category: String) {
        viewModelScope.launch {
            _productsLiveData.value = Resource.Loading
            val result = productsRepo.getProductsByCategory(category)
            _productsLiveData.value = result
        }
    }

    fun getUserFavoritesIDsByCategory(category: String) {
        viewModelScope.launch {
            _userFavoritesIDsLiveData.value = Resource.Loading
            val favoritesIDsResult = productsRepo.getUserFavoritesIDsByCategory(category)
            _userFavoritesIDsLiveData.value = favoritesIDsResult
        }
    }

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

    fun setSelectedCategory(selectedCategory: String) {
        _selectedCategory.value = selectedCategory
    }

    fun getUserFavorites() {
        viewModelScope.launch {
            _userFavoritesLiveData.value = Resource.Loading
            val favoriteProductsResult = productsRepo.getUserFavorites()
            _userFavoritesLiveData.value = favoriteProductsResult
        }
    }

}