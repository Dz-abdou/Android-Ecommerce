package com.example.eccomercek

import android.app.Activity
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.util.*
import kotlin.collections.ArrayList

class AddShippingAddressFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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



        return inflater.inflate(R.layout.fragment_add_shipping_address, container, false)
    }

    override fun onResume() {
        super.onResume()
        val activity: Activity? = activity
        if (activity != null) {
            (activity as MainActivity?)?.supportActionBar?.title = "Add Shipping Address"
        }
    }
}