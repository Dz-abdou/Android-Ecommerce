package com.example.ecommerce.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.models.OrderModel
import com.example.ecommerce.data.models.ProductInCartModel
import com.example.ecommerce.data.repositories.OrdersRepo
import com.example.ecommerce.data.repositories.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val ordersRepo: OrdersRepo): ViewModel() {

    private val _ordersLiveData = MutableLiveData< Resource<Map<String, List<OrderModel>>>?>(null)
    val ordersLiveData: LiveData< Resource<Map<String, List<OrderModel>>>?> = _ordersLiveData

    private val _selectedOrderLIveData = MutableLiveData<OrderModel>(null)
    val selectedOrderLIveData: LiveData<OrderModel> = _selectedOrderLIveData

    private val _orderDetailsLiveData = MutableLiveData<Resource<List<ProductInCartModel>>>(null)
    val orderDetailsLiveData: LiveData<Resource<List<ProductInCartModel>>> = _orderDetailsLiveData

    init {
        viewModelScope.launch {
            _ordersLiveData.value = Resource.Loading
            val result = ordersRepo.getAllOrders()
            _ordersLiveData.value = result
        }
    }

    fun setSelectedOrder(orderModel: OrderModel) {
        _selectedOrderLIveData.value = orderModel
    }

    fun getOrderDetails(orderID: String) {
        viewModelScope.launch {
            _orderDetailsLiveData.value = Resource.Loading
            _orderDetailsLiveData.value = ordersRepo.getOrderDetails(orderID)
        }
    }
}