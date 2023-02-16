package com.example.ecommerce.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.ui.adapters.CartRvAdapter
import com.example.ecommerce.databinding.FragmentCartBinding
import com.example.ecommerce.data.models.ProductInCartModel
import com.example.ecommerce.data.models.ShippingAddressModel
import com.example.ecommerce.data.repositories.Resource
import com.example.ecommerce.util.logMessage
import com.example.ecommerce.util.showSnackBar
import com.example.ecommerce.ui.viewModels.CartViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


class CartFragment : Fragment() {

    private lateinit var binding:FragmentCartBinding
    private lateinit var viewModel: CartViewModel
    private lateinit var productInCartModelList: ArrayList<ProductInCartModel>
    private var selectedShippingAddress: ShippingAddressModel? = null
    private lateinit var adapter: CartRvAdapter
    private var total = 0.0
    private var placeOrderClicked = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCartBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[CartViewModel::class.java]

        viewModel.getCartProducts()
        viewModel.getUserSelectedShippingAddress()

        viewModel.cartLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Failure -> {
                    binding.progressCircular.visibility = View.GONE
                    binding.cartEmptyIllu.visibility = View.VISIBLE
                }
                Resource.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    productInCartModelList = it.result as ArrayList<ProductInCartModel>
                    logMessage("success $productInCartModelList")
                    if (productInCartModelList.isEmpty())
                        binding.cartEmptyIllu.visibility = View.VISIBLE
                    else {
                        setCartRv()
                        total = 0.0
                        for (product in productInCartModelList) {
                            total += product.productPrice * product.productQuantity
                        }
                        binding.subTotalPriceTV.text = "$" + String.format("%.2f", total)
                        binding.ShippingPriceTV.text = "$20.00"
                        binding.bagTotalPriceTV.text = "$" + String.format("%.2f", total+20)
                    }
                }
                null -> {}
            }
        })

        viewModel.selectedShippingAddress.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Failure -> {}
                Resource.Loading -> {}
                is Resource.Success -> {
                    selectedShippingAddress = it.result
                }
            }
        })

        viewModel.addOrderResponse.observe(viewLifecycleOwner, Observer {
            if(placeOrderClicked) {
                when(it) {
                    is Resource.Failure -> {
                        showSnackBar(binding.fragmentCartConstraintLayout,
                            "There was an error placing the order!\n" +
                                    "Please feel free to try again later.")
                        binding.progressCircular.visibility = View.INVISIBLE

                    }
                    Resource.Loading -> {
                        binding.progressCircular.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressCircular.visibility = View.INVISIBLE
                        if (it.result) {
                            viewModel.emptyCart()
                            productInCartModelList = ArrayList()
                            adapter.emptyRv()
                            binding.cartEmptyIllu.visibility = View.VISIBLE
                        }
                        else
                            showSnackBar(binding.fragmentCartConstraintLayout,
                                "There was an error placing the order!\n" +
                                        "Please feel free to try again later.")
                    }
                }
            }
        })

        binding.checkoutButton.setOnClickListener {
            if(productInCartModelList != null && productInCartModelList.isNotEmpty()) {
                if (selectedShippingAddress == null){
                    AlertDialog.Builder(requireContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                        .setTitle("We couldn't find a selected shipping address!")
                        .setMessage("Please select an existing Address or add a new one then try again.")
                        .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                            val navOptions = NavOptions.Builder().setEnterAnim(
                                androidx.fragment.R.animator.fragment_open_enter).build()
                            val bundle = Bundle()
                            dialog.dismiss()
                            findNavController().navigate(R.id.action_cartFragment_to_shippingAdressesFragment,bundle, navOptions)
                        }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()
                        })
                        .show()
                } else {
                    binding.progressCircular.visibility = View.VISIBLE
                    var canceled = false
                    Snackbar.make(binding.fragmentCartConstraintLayout, "This is main activity", Snackbar.LENGTH_LONG)
                        .setAction("CANCEL", View.OnClickListener {
                            canceled = true
                            binding.progressCircular.visibility = View.INVISIBLE
                        })
                        .setActionTextColor(resources.getColor(R.color.accent))
                        .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>(){
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                super.onDismissed(transientBottomBar, event)
                                if (!canceled) {
                                    logMessage("orderPlaced");
                                    viewModel.addOrder(productInCartModelList, selectedShippingAddress!!)
                                    placeOrderClicked = true
                                }

                            }
                        })
                        .show()
                }
            } else
                Snackbar.make(binding.fragmentCartConstraintLayout, "Please fill you cart first!", Snackbar.LENGTH_SHORT).show()
        }
        return binding.root
    }

    private fun setCartRv() {
        adapter = context?.let { CartRvAdapter(it, productInCartModelList) }!!
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.cartRecyclerView.layoutManager = layoutManager
        binding.cartRecyclerView.adapter = adapter

        adapter?.setOnItemClickListener(object : CartRvAdapter.OnItemClickListener{
            override fun onIncreaseClick(context: Context, position: Int, view: View) {
                val quantityTv = view as TextView
                val quantity = quantityTv.text.toString().toInt()+1
                if(quantity <= 10) {
                    quantityTv.text = quantity.toString()
                    productInCartModelList[position].productQuantity++
                    total += productInCartModelList[position].productPrice.toFloat()
                    binding.subTotalPriceTV.text = "$" + String.format("%.2f", total)
                    binding.bagTotalPriceTV.text = "$" + String.format("%.2f", total+20)
                }
            }

            override fun onDecreaseClick(context: Context, position: Int, view: View) {
                val quantityTv = view as TextView
                val quantity = quantityTv.text.toString().toInt() - 1
                total -= productInCartModelList[position].productPrice.toFloat()
                binding.subTotalPriceTV.text = "$" + String.format("%.2f", total)
                binding.bagTotalPriceTV.text = "$" + String.format("%.2f", total+20)
                if (quantity >= 1) {
                    quantityTv.text = quantity.toString()
                    productInCartModelList[position].productQuantity--
                } else {
                    viewModel.removeProductFromCart(productInCartModelList[position].productID)
                    adapter.removeItem(position)
                    if (productInCartModelList.isEmpty())
                        binding.cartEmptyIllu.visibility = View.VISIBLE
                }
            }
        })


    }

}