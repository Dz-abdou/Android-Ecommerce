package com.example.ecommerce.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.data.models.OrderModel
import com.example.ecommerce.data.models.ProductInCartModel
import com.example.ecommerce.data.repositories.Resource
import com.example.ecommerce.databinding.FragmentOrderDetailsBinding
import com.example.ecommerce.ui.adapters.OrderDetailsRvAdapter
import com.example.ecommerce.ui.viewModels.OrdersViewModel
import com.example.ecommerce.util.Constants
import com.google.firestore.v1.StructuredQuery.Order


class OrderDetailsFragment : Fragment() {

    private lateinit var binding: FragmentOrderDetailsBinding
    private lateinit var viewModel: OrdersViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[OrdersViewModel::class.java]
        viewModel.selectedOrderLIveData.observe(viewLifecycleOwner, Observer {
            setViews(it)
            viewModel.getOrderDetails(it.orderID)
        })
        viewModel.orderDetailsLiveData.observe(viewLifecycleOwner, Observer{
            when(it) {
                is Resource.Failure -> {
                    binding.progressCircular.visibility = View.GONE
                }
                Resource.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    val products = it.result
                    if (products.isNotEmpty())
                        setProductsRV(it.result as ArrayList<ProductInCartModel>)
                }
            }
        })
        return binding.root
    }

    private fun setViews(orderModel: OrderModel){
        binding.orderNumberTV.text = orderModel.orderID.split("-")[1]
        val date = orderModel.orderDate.toDate().toLocaleString().split(" ")
        binding.orderDateTv.text = date[0] + " " + date[1] + " " + date[2]
        binding.shippingAddressTV.text = orderModel.shippingAddress
        binding.totalAmountTV.text = "$${orderModel.totalAmount}"
        binding.QuantityTV.text = orderModel.numberOfProducts.toString()
        binding.statusTV.text = orderModel.status
        if (orderModel.status == Constants.PROCESSING) {
            binding.trackingNumberTV.text = "-"
            binding.statusTV.setTextColor(resources.getColor(R.color.orange))
            binding.deliveryMethodTv.text = "-"
        } else {
            binding.trackingNumberTV.text = orderModel.trackingCode
            binding.statusTV.setTextColor(resources.getColor(R.color.green))
            binding.deliveryMethodTv.text = orderModel.deliveryMethod
        }
    }

    private fun setProductsRV(products: ArrayList<ProductInCartModel>) {
        val adapter = context?.let { OrderDetailsRvAdapter(it, products) }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }

}