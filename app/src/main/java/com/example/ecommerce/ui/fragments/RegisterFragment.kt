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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentRegisterBinding
import com.example.ecommerce.data.repositories.Resource
import com.example.ecommerce.ui.activities.MainActivity
import com.example.ecommerce.util.Constants
import com.example.ecommerce.util.isEmailValid
import com.example.ecommerce.ui.viewModels.AuthViewModel


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var signinPd: ProgressDialog
    private var email: String = ""
    private var fullName: String = ""
    private var age: String = ""
    private var password: String = ""
    private var confirmedPassword: String = ""
    private var sex: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val sharedPreference = activity?.getSharedPreferences(Constants.TAG, Context.MODE_PRIVATE)

        setSexSpinner()

        val viewmodel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        signinPd = ProgressDialog(context)
        signinPd.setMessage("Signing in...")

        viewmodel.signupLivedata.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Failure -> {
                    Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                    it.exception.localizedMessage?.let { it1 -> Log.d(Constants.TAG, it1) }
                    signinPd.hide()
                }
                is Resource.Loading ->
                {
                    signinPd.show()
                }
                is Resource.Success -> {
                    viewmodel.createAccount(email, fullName, age.toInt(), sex)
                }
                null -> signinPd.hide()
            }
        })

        viewmodel.accountLivedata.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Failure -> {
                    Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                    it.exception.localizedMessage?.let { it1 -> Log.d(Constants.TAG, it1) }
                    signinPd.hide()
                }
                Resource.Loading -> {

                }
                is Resource.Success -> {
                    Toast.makeText(context, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
                    signinPd.hide()
                    val editor = sharedPreference?.edit()
                    editor?.putBoolean("rememberMe", binding.rememberMeCheckBox.isChecked)
                    editor?.apply()
                    signinPd.dismiss()
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                }
                null -> signinPd.hide()
            }

        })
        binding.signupButton.setOnClickListener {
            email = binding.emailTIET.text.toString().trim()
            password = binding.passwordTTIET.text.toString()
            confirmedPassword = binding.confirmPasswordTIET.text.toString()
            fullName = binding.fullNameTIET.text.toString().trim()
            age = binding.ageTIET.text.toString().trim()
            sex = binding.sexSpinner.selectedItem.toString().trim()

            binding.emailTIL.error = null
            binding.fullNameTIL.error = null
            binding.ageTIL.error = null
            binding.fullNameTIL.error = null
            binding.passwordTIL.error = null
            binding.confirmPasswordTIL.error = null

            if(TextUtils.isEmpty(email)) {
                binding.emailTIL.error = "Please enter your email!"
            } else if (!isEmailValid(email)) {
                binding.emailTIL.error = "Invalid email!"
            } else if (fullName.isEmpty()) {
                binding.fullNameTIL.error = "Please enter your full Name!"
            } else if (age.isEmpty() || age.toInt() < 13 || age.toInt() > 115) {
                binding.ageTIL.error = "Please enter your Age!"
            } else if (password.isEmpty()) {
                binding.ageTIET.error = "Please enter your password!"
            } else if (sex.isEmpty() || sex == "Sex") {
                Toast.makeText(context, "Please enter your gender!", Toast.LENGTH_SHORT).show()
            }else if(password.length < 8) {
                binding.passwordTIL.error = "Password must be at least 6 chars long!"
            } else if(confirmedPassword != password){
                binding.confirmPasswordTIL.error = "Password doesn't match!"
            } else {
                viewmodel.signup(email, password, fullName)
            }

        }

        return  binding.root
    }

    private fun setSexSpinner() {
        val sex = arrayOf("Sex", "Male", "Female", "Other")

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item ,illnessesList);
        val arrayAdapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
            requireContext(), R.layout.spinner_item, sex
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(context.resources.getColor(R.color.text_gray_darker))

                } else {
                    tv.setTextColor(context.resources.getColor(R.color.bg_black))
                }
                return view
            }
        }

        binding.sexSpinner.adapter = arrayAdapter

        binding.sexSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItemText = parent.getItemAtPosition(position) as String
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    activity?.findViewById<TextView>(R.id.sexSpinnerTV)?.setTextColor(resources.getColor(R.color.text_gray))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

}