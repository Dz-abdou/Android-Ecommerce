package com.example.ecommerce.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.models.ProductInCartModel
import com.example.ecommerce.data.models.ShippingAddressModel
import com.example.ecommerce.data.repositories.AuthRepo
import com.example.ecommerce.data.repositories.CartRepo
import com.example.ecommerce.data.repositories.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepo: CartRepo): ViewModel() {

    private val _cartLiveData = MutableLiveData<Resource<List<ProductInCartModel>>?>(null)
    val cartLiveData: LiveData<Resource<List<ProductInCartModel>>?> = _cartLiveData

    private val _selectedShippingAddress = MutableLiveData<Resource<ShippingAddressModel?>>(null)
    val selectedShippingAddress: LiveData<Resource<ShippingAddressModel?>> = _selectedShippingAddress

    private val _addOrderResponse = MutableLiveData<Resource<Boolean>>(null)
    val addOrderResponse: LiveData<Resource<Boolean>> = _addOrderResponse

    fun getCartProducts() {
        viewModelScope.launch {
            _cartLiveData.value = Resource.Loading
            val result = cartRepo.getUserCart()
            _cartLiveData.value = result
        }
    }

    fun removeProductFromCart(productID: String) {
        viewModelScope.launch {
            cartRepo.removeProductFromCart(productID)
        }
    }

    fun getUserSelectedShippingAddress() {
        viewModelScope.launch {
            _selectedShippingAddress.value = Resource.Loading
            val result = cartRepo.getUserSelectedShippingAddress()
            _selectedShippingAddress.value = result
        }
    }

    fun addOrder(cartList: ArrayList<ProductInCartModel>, shippingAddress: ShippingAddressModel) {
        viewModelScope.launch {
            _addOrderResponse.value = Resource.Loading
            _addOrderResponse.value = cartRepo.addOrder(cartList, shippingAddress)
        }
    }

    fun emptyCart() {
        viewModelScope.launch {
            cartRepo.emptyCart()
        }
    }



}