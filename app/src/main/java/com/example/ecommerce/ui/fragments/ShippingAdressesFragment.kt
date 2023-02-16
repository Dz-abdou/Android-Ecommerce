package com.example.ecommerce.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.ui.adapters.ShippingAddressRvAdapter
import com.example.ecommerce.databinding.FragmentShippingAdressesBinding
import com.example.ecommerce.data.models.ShippingAddressModel
import com.example.ecommerce.data.repositories.Resource
import com.example.ecommerce.util.Constants
import com.example.ecommerce.util.logMessage
import com.example.ecommerce.ui.viewModels.ProductsViewModel
import com.example.ecommerce.ui.viewModels.ShippingAddressesViewModel


class ShippingAdressesFragment : Fragment() {

    private lateinit var binding: FragmentShippingAdressesBinding
    private lateinit var viewModel: ShippingAddressesViewModel
    private var shippingAddresses = ArrayList<ShippingAddressModel>()
    private var fromCartFragment = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =  FragmentShippingAdressesBinding.inflate(inflater, container, false)


        viewModel = ViewModelProvider(requireActivity())[ShippingAddressesViewModel::class.java]
        viewModel.getShippingAddresses()
        viewModel.shippingAddressesLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Failure -> {}
                Resource.Loading -> {}
                is Resource.Success -> {
                    shippingAddresses = it.result as ArrayList<ShippingAddressModel>
                    setAddressesAdapter()
                }
                null -> TODO()
            }
        })

        binding.addAddressFab.setOnClickListener {
            findNavController().navigate(R.id.action_shippingAdressesFragment_to_addShippingAddressFragment)
        }

        return binding.root
    }

    private fun setAddressesAdapter() {
        val addressesRv = binding.shippingAddressesRV
        addressesRv.layoutManager = LinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false)
        val addressesAdapter =
            context?.let { ShippingAddressRvAdapter(it, shippingAddresses) }
        addressesRv.adapter = addressesAdapter

        addressesAdapter?.setOnItemClickListener(object : ShippingAddressRvAdapter.OnItemClickListener{
            override fun onItemClick(context: Context, position: Int) {
            }

            override fun onUseThisAddressClick(position: Int, checkBox: CheckBox) {
                var currentUseAddressPos = -1
                for (address in shippingAddresses) {
                    if (address.useThisAddress)
                        currentUseAddressPos = shippingAddresses.indexOf(address)
                }
                logMessage(currentUseAddressPos.toString())
                if (currentUseAddressPos == position) {
                    viewModel.removeUsedAddress(shippingAddresses[position].address + " " + shippingAddresses[position].city)
                    addressesAdapter.uncheckCheckbox(currentUseAddressPos)
                }
                else if (currentUseAddressPos != -1) {
                    viewModel.removeUsedAddress(shippingAddresses[currentUseAddressPos].address + " " + shippingAddresses[currentUseAddressPos].city)
                    viewModel.addUsedAddress(shippingAddresses[position].address + " " + shippingAddresses[position].city)
                    addressesAdapter.uncheckCheckbox(currentUseAddressPos)
                    checkBox.isChecked = true
                    shippingAddresses[currentUseAddressPos].useThisAddress = false
                    shippingAddresses[position].useThisAddress = true
                } else {
                    viewModel.addUsedAddress(shippingAddresses[position].address + " " + shippingAddresses[position].city)
                    checkBox.isChecked = true
                    shippingAddresses[position].useThisAddress = true
                }

            }

        })
    }


}