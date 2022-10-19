package com.example.eccomercek

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eccomercek.adapters.ShippingAddressRvAdapter
import com.example.eccomercek.databinding.FragmentShippingAdressesBinding
import com.example.eccomercek.models.ShippingAddressModel


class ShippingAdressesFragment : Fragment() {

    private lateinit var binding: FragmentShippingAdressesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentShippingAdressesBinding.inflate(inflater, container, false)

        val addressesRv = binding.shippingAddressesRV
        addressesRv.layoutManager = LinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false)

        val addressesAdapter =
            ShippingAddressRvAdapter(ShippingAddressModel.MockList.getModel() as ArrayList<ShippingAddressModel>)

        addressesRv.adapter = addressesAdapter

        binding.addAddressFab.setOnClickListener {
            findNavController().navigate(R.id.action_shippingAdressesFragment_to_addShippingAddressFragment)
        }

        return binding.root
    }




}