package com.example.ecommerce.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.models.ShippingAddressModel
import com.example.ecommerce.data.repositories.Resource
import com.example.ecommerce.data.repositories.ShippingAddressRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShippingAddressesViewModel @Inject constructor(private val shippingAddressRepo: ShippingAddressRepo): ViewModel() {


    private val _shippingAddressesLiveData = MutableLiveData<Resource<List<ShippingAddressModel>>?>(null)
    val shippingAddressesLiveData: LiveData<Resource<List<ShippingAddressModel>>?> = _shippingAddressesLiveData

    fun getShippingAddresses() {
        viewModelScope.launch {
            _shippingAddressesLiveData.value = Resource.Loading
            val result = shippingAddressRepo.getShippingAddresses()
            _shippingAddressesLiveData.value = result
        }
    }

    fun addShippingAddress(shippingAddressModel: ShippingAddressModel) {
        viewModelScope.launch{
            shippingAddressRepo.addUserShippingAddress(shippingAddressModel)
        }
    }

    fun removeShippingAddress(addressID: String) {
        viewModelScope.launch {
            shippingAddressRepo.removeShippingAddress(addressID)
        }
    }

    fun removeUsedAddress(addressID: String) {
        viewModelScope.launch {
            shippingAddressRepo.removeAddressUsed(addressID)
        }
    }

    fun addUsedAddress(addressID: String) {
        viewModelScope.launch {
            shippingAddressRepo.addAddressUsed(addressID)
        }
    }
}