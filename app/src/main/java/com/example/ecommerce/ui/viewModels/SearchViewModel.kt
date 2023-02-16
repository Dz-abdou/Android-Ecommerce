package com.example.ecommerce.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.models.ProductModel
import com.example.ecommerce.data.repositories.ProductsRepo
import com.example.ecommerce.data.repositories.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val productsRepo: ProductsRepo) : ViewModel() {

    private val _searchedProductsLiveData = MutableLiveData<Resource<ArrayList<ProductModel>>>(null)
    val searchedProductsLiveData: LiveData<Resource<ArrayList<ProductModel>>> = _searchedProductsLiveData

    fun getSearchedProducts(criteria: String, searchString: String, categories: ArrayList<String>) {
        viewModelScope.launch {
            _searchedProductsLiveData.value = Resource.Loading
            val searchResult = productsRepo.searchProduct(criteria, searchString, categories)
            _searchedProductsLiveData.value = searchResult
        }
    }
}