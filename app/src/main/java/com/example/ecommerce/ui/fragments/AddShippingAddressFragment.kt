package com.example.ecommerce.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerce.ui.activities.MainActivity
import com.example.ecommerce.databinding.FragmentAddShippingAddressBinding
import com.example.ecommerce.data.models.ShippingAddressModel
import com.example.ecommerce.ui.viewModels.ShippingAddressesViewModel
import java.util.*
import kotlin.collections.ArrayList

class AddShippingAddressFragment : Fragment() {

    private lateinit var binding: FragmentAddShippingAddressBinding
    private lateinit var viewModel: ShippingAddressesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentAddShippingAddressBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ShippingAddressesViewModel::class.java]


        val locales: Array<Locale> = Locale.getAvailableLocales()
        val countries = ArrayList<String>()
        for (locale in locales) {
            val country: String = locale.displayCountry
            if (country.trim { it <= ' ' }.isNotEmpty() && !countries.contains(country)) {
                countries.add(country)
            }
        }
        countries.sort()
        Log.i("countries", countries.toString())

        val adapter: ArrayAdapter<String>? =
            context?.let { ArrayAdapter(it, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, countries ) }

        binding.countriesAutoCompleteTV.setAdapter(adapter)

        binding.saveAddress.setOnClickListener {
            val address  = binding.addressTIET.text.toString().trim()
            val city  = binding.cityTIET.text.toString().trim()
            val zipCode  = binding.zipCodeTIET.text.toString().trim()
            val province  = binding.stateProvinceRegionTIET.text.toString().trim()
            val country  = binding.countriesAutoCompleteTV.text.toString().trim()

            if(address.isEmpty())
                binding.addressTIL.error = "Please enter your address!"
            else if(city.isEmpty())
                binding.cityTIET.error = "Please enter your city!"
            else if(zipCode.isEmpty())
                binding.zipCodeTIET.error = "Please enter your zipCode!"
            else if(province.isEmpty())
                binding.stateProvinceRegionTIET.error = "Please enter your province/state/region!"
            else if(country.isEmpty())
                binding.countriesAutoCompleteTV.error = "Please enter your country!"
            else
                viewModel.addShippingAddress(ShippingAddressModel(address, city,province,zipCode, country, false))

        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val activity: Activity? = activity
        if (activity != null) {
            (activity as MainActivity?)?.supportActionBar?.title = "Add Shipping Address"
        }
    }


}