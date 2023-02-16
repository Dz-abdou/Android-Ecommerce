package com.example.ecommerce.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.ui.adapters.OrdersRvAdapter
import com.example.ecommerce.databinding.FragmentOrdersBinding
import com.example.ecommerce.data.models.OrderModel
import com.example.ecommerce.data.repositories.Resource
import com.example.ecommerce.ui.viewModels.AuthViewModel
import com.example.ecommerce.ui.viewModels.OrdersViewModel
import com.example.ecommerce.util.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.firestore.v1.StructuredQuery.Order


class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    private lateinit var viewModel: OrdersViewModel
    private var processingOrders = ArrayList<OrderModel>()
    private var deliveredOrders = ArrayList<OrderModel>()
    private lateinit var processingOrdersAdapter: OrdersRvAdapter
    private lateinit var deliveredOrdersAdapter: OrdersRvAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[OrdersViewModel::class.java]
        setOrderChips()


        viewModel.ordersLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Failure -> {}
                Resource.Loading -> {}
                is Resource.Success -> {
                    processingOrders = it.result[Constants.PROCESSING] as ArrayList
                    deliveredOrders = it.result[Constants.DELIVERED] as ArrayList
                    setDeliveredRv()
                    setProcessingRv()
                }
                null -> {}
            }
        })

        //make Delivered Rv out of the screen
        context?.let { moveViewOutsideOfTheScreenRight(binding.deliveredOrdersRV, it) }

        return binding.root
    }

    private fun setProcessingRv() {
        binding.processingOrdersRV.layoutManager = LinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false)
        val processingOrdersAdapter = OrdersRvAdapter(processingOrders, true)
        binding.processingOrdersRV.adapter = processingOrdersAdapter

        processingOrdersAdapter.setOnItemClickListener(object : OrdersRvAdapter.OnItemClickListener{
            override fun onDetailsClick(position: Int) {
                viewModel.setSelectedOrder(processingOrders[position])
                findNavController().navigate(R.id.action_ordersFragment_to_orderDetailsFragment)
            }
        })
    }

    private fun setDeliveredRv() {
        binding.deliveredOrdersRV.layoutManager = LinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false)
        val processingOrdersAdapter = OrdersRvAdapter(deliveredOrders, false)
        binding.deliveredOrdersRV.adapter = processingOrdersAdapter

        processingOrdersAdapter.setOnItemClickListener(object : OrdersRvAdapter.OnItemClickListener{
            override fun onDetailsClick(position: Int) {
                viewModel.setSelectedOrder(deliveredOrders[position])
                findNavController().navigate(R.id.action_ordersFragment_to_orderDetailsFragment)
            }
        })
    }

    private fun setOrderChips() {

        val typesOfOrders: ArrayList<String> = ArrayList()
        typesOfOrders.add("Processing")
        typesOfOrders.add("Delivered")
        for (typeOfOrder in typesOfOrders) {
            val chip: Chip = Chip(context)
            val chipDrawable = context?.let {
                ChipDrawable.createFromAttributes(
                    it,
                    null,
                    0,
                    R.style.CustomChipChoice
                )
            }
            chipDrawable?.let { chip.setChipDrawable(it) }
            chip.text = typeOfOrder
            val padding =
                resources.getDimension(R.dimen.chip_padding) / resources.displayMetrics.density
            chip.chipStartPadding = padding
            chip.chipEndPadding = padding
            chip.isClickable = true
            chip.isCheckable = true
            chip.isFocusable = true
            chip.setTextAppearance(R.style.AppTheme_ChipText)
            chip.setTextColor(resources.getColor(R.color.text_gray))

            if (typesOfOrders.indexOf(typeOfOrder) == 0) {
                chip.isChecked = true
                chip.setTextColor(resources.getColor(R.color.bg_black))
            }

            chip.setOnCheckedChangeListener { compoundButton, b ->
                if (b)
                    compoundButton.setTextColor(resources.getColor(R.color.bg_black))
                else
                    compoundButton.setTextColor(resources.getColor(R.color.text_gray))

                if (chip.text == "Delivered") {
                    context?.let {moveViewOutOfScreenLeftWithAnimation(binding.processingOrdersRV, it)}
                    moveViewToInsideOfTheScreenWithAnimation(binding.deliveredOrdersRV)
                }
                if (chip.text == "Processing") {
                    context?.let { moveViewOutOfScreenRightWithAnimation(binding.deliveredOrdersRV, it) }
                    moveViewToInsideOfTheScreenWithAnimation(binding.processingOrdersRV)

                }

            }
            binding.orderChips.addView(chip)
        }
    }
}