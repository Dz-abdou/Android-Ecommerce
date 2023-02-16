package com.example.ecommerce.ui.fragments

import android.accessibilityservice.GestureDescription.StrokeDescription
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.data.models.ProductModel
import com.example.ecommerce.data.repositories.Resource
import com.example.ecommerce.databinding.FragmentHomeBinding
import com.example.ecommerce.databinding.FragmentSearchResultBinding
import com.example.ecommerce.ui.activities.ProductActivity
import com.example.ecommerce.ui.adapters.ProductRvAdapter
import com.example.ecommerce.ui.viewModels.ProductsViewModel
import com.example.ecommerce.ui.viewModels.SearchViewModel
import com.example.ecommerce.util.Constants
import dagger.hilt.android.AndroidEntryPoint

class SearchResultFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var criteria: String
    private lateinit var searchString: String
    private lateinit var categories: ArrayList<String>
    private lateinit var products: ArrayList<ProductModel>
    private lateinit var rvAdapter: ProductRvAdapter
    private lateinit var productsFavoriteIDs: ArrayList<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        productsViewModel = ViewModelProvider(requireActivity())[ProductsViewModel::class.java]
        searchViewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]

        categories = ArrayList()
        products = ArrayList()
        productsFavoriteIDs = ArrayList()

        criteria = productsViewModel.searchQuery[Constants.CRITERIA] as String
        searchString = productsViewModel.searchQuery[Constants.SEARCH_VALUE] as String
        categories = productsViewModel.searchQuery[Constants.CATEGORIES] as ArrayList<String>

        searchViewModel.getSearchedProducts(criteria, searchString, categories)

        productsViewModel.userFavoritesIDsLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Failure -> {
                }
                Resource.Loading -> {
                }
                is Resource.Success -> {
                    productsFavoriteIDs = it.result
                }
                null -> {}
            }
        })

        searchViewModel.searchedProductsLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Failure -> {
                    Toast.makeText(
                        context,
                        "Failed trying to retrieve products!",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(Constants.TAG, it.exception.toString())
                    it.exception.printStackTrace()
                    binding.progressCircular.visibility = View.GONE
                }
                Resource.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    products = it.result as ArrayList<ProductModel>
                    setProductsRV()
                }
                null -> {
                    binding.progressCircular.visibility = View.GONE
                }
            }
        })

        return binding.root
    }

    private fun setProductsRV() {
        binding.productsRecyclerView.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        rvAdapter =
            context?.let { ProductRvAdapter(it, products, productsFavoriteIDs) }!!
        binding.productsRecyclerView.adapter = rvAdapter
        rvAdapter.setOnItemClickListener(object : ProductRvAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val itemClicked = products[position]
                val intent = Intent(context, ProductActivity::class.java)
                intent.putExtra(Constants.EXTRA_KEY_PRODUCT, itemClicked)
                intent.putExtra(
                    Constants.EXTRA_KEY_IN_FAVORITE,
                    productsFavoriteIDs.contains(products[position].productName)
                )
                startActivity(intent)
            }

            override fun onAddToFavoritesClicked(position: Int, view: View?) {
                val favoriteButton = view as ImageView
                if (!productsFavoriteIDs.contains(products[position].productName)) {
                    favoriteButton.setImageResource(R.drawable.ic_heart_black)
                    productsViewModel.addProductToFavorites(
                        products[position].productName,
                        products[position].productCategory
                    )
                } else {
                    favoriteButton.setImageResource(R.drawable.ic_heart_white)
                    productsViewModel.removeProductFromFavorite(
                        products[position].productName,
                        products[position].productCategory

                    )
                }
            }
        })


    }

}