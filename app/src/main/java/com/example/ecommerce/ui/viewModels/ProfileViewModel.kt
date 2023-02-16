package com.example.ecommerce.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.ecommerce.data.repositories.ProfileRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepo: ProfileRepo): ViewModel() {

    fun logout() {
        profileRepo.logout()
    }
}