package com.example.ecommerce.ui.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentLoginBinding
import com.example.ecommerce.data.repositories.Resource
import com.example.ecommerce.ui.activities.MainActivity
import com.example.ecommerce.util.Constants
import com.example.ecommerce.util.isEmailValid
import com.example.ecommerce.ui.viewModels.AuthViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginPd: ProgressDialog
    private lateinit var viewmodel: AuthViewModel
    private var sharedPreference = activity?.getSharedPreferences(Constants.TAG, Context.MODE_PRIVATE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)
        loginPd = ProgressDialog(context)
        loginPd.setMessage("Please wait.")

        viewmodel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        viewmodel.loginLivedata.observe(viewLifecycleOwner, Observer {

            when(it) {
                is Resource.Failure -> {
                    Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                    it.exception.localizedMessage?.let { it1 -> Log.d(Constants.TAG, it1)
                        loginPd.hide()}
                }
                is Resource.Loading ->
                {
                    loginPd.show()
                }
                is Resource.Success -> {
                    val editor = sharedPreference?.edit()
                    editor?.putBoolean("rememberMe", binding.rememberMeCheckBox.isChecked)
                    editor?.apply()
                    loginPd.hide()
                    loginPd.dismiss()
                    activity?.finish()
                    startActivity(Intent(activity, MainActivity::class.java))
                }
                null -> loginPd.hide()
            }
        })

        binding.loginButton.setOnClickListener {
            val email = binding.emailTIET.text.toString().trim()
            val password = binding.passwordTTIET.text.toString()
            if(TextUtils.isEmpty(email)){
                binding.emailTIL.error = "Please enter your email!"
            } else if(TextUtils.isEmpty(password))
            {
                binding.passwordTIL.error = "Please enter your password!"
            } else if (!isEmailValid(email)) {
                binding.emailTIL.error = "Invalid email!"

            } else if (password.length < 6){
                binding.passwordTIL.error = "password must be at lease 6 chars long!"

            } else {
                viewmodel.login(email, password.trim())
            }


        }


        binding.signupTV.setOnClickListener {
            val fragment: Fragment = RegisterFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.loginFragmentContainerView , fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return binding.root
    }


}