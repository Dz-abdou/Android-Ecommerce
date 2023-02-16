package com.example.ecommerce.ui.viewModels

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.repositories.AuthRepo
import com.example.ecommerce.data.repositories.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepo: AuthRepo): ViewModel() {

    private val _loginLivedata = MutableLiveData<Resource<FirebaseUser>?>(null)
    val loginLivedata: LiveData<Resource<FirebaseUser>?> = _loginLivedata

    private val _signupLivedata = MutableLiveData<Resource<FirebaseUser>?>(null)
    val signupLivedata: LiveData<Resource<FirebaseUser>?> = _signupLivedata

    private val _accountLivedata = MutableLiveData<Resource<Any>?>(null)
    val accountLivedata: LiveData<Resource<Any>?> = _accountLivedata

    val currentUser: FirebaseUser?
        get() = authRepo.currentUser



    fun login(email: String, password: String) = viewModelScope.launch{
        _loginLivedata.value = Resource.Loading
        val result = authRepo.login(email, password)
        _loginLivedata.value = result
    }

    fun signup(email: String, password: String, name: String) = viewModelScope.launch{
        _signupLivedata.value = Resource.Loading
        val result = authRepo.signup(name,email, password)
        _signupLivedata.value = result
    }

    fun createAccount(email: String, name: String, age: Number, sex: String) = viewModelScope.launch{
        _accountLivedata.value = Resource.Loading
        val result = authRepo.createUserInFirestore(name,age, sex, email)
        _accountLivedata.value = result
    }

    fun logout() {
        authRepo.logout()
        _loginLivedata.value = null
        _signupLivedata.value = null
        _accountLivedata.value = null
    }
}