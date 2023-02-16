package com.example.ecommerce.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.ui.adapters.ProductRvAdapter
import com.example.ecommerce.databinding.FragmentFavoritesBinding
import com.example.ecommerce.data.models.ProductModel
import com.example.ecommerce.data.repositories.Resource
import com.example.ecommerce.ui.activities.ProductActivity
import com.example.ecommerce.util.Constants
import com.example.ecommerce.util.logMessage
import com.example.ecommerce.ui.viewModels.ProductsViewModel


class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewmodel: ProductsViewModel
    private var products = ArrayList<ProductModel>()
    private var adapter: ProductRvAdapter? = null
    // this flag is used to know where the observer is called for the first time or when refreshed
    private var refreshed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        viewmodel = ViewModelProvider(requireActivity())[ProductsViewModel::class.java]

        viewmodel.userFavoritesLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Failure -> {
                    Toast.makeText(context, "Failed trying to retrieve users favorite products!", Toast.LENGTH_SHORT).show()
                    Log.d(Constants.TAG, it.exception.message.toString())
                    it.exception.printStackTrace()
                    if (!refreshed)
                        binding.favoritesIllustration.visibility = View.VISIBLE
                    binding.swipeContainer.isRefreshing = false
                }
                Resource.Loading -> {
                }
                is Resource.Success -> {
                    products = it.result
                    for (product in products)
                    if (refreshed && adapter != null){
                        adapter?.clear()
                        adapter?.addAll(products)
                    } else {
                        setFavoritesRV()
                    }
                    binding.swipeContainer.isRefreshing = false
                    if(products.isEmpty())
                        binding.favoritesIllustration.visibility = View.VISIBLE
                    else
                        binding.favoritesIllustration.visibility = View.GONE
                }
                null -> {}
            }
        })

        binding.swipeContainer.setOnRefreshListener {
            refreshed = true
            viewmodel.getUserFavorites()
        }
        binding.swipeContainer.setColorSchemeResources(R.color.accent);

        return binding.root
    }

    private fun setFavoritesRV() {
        binding.favoriteProductsRecyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        adapter = context?.let { ProductRvAdapter(it, products, null) }
        binding.favoriteProductsRecyclerView.adapter= adapter
        adapter?.setOnItemClickListener(object : ProductRvAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val itemClicked = products[position]
                val intent = Intent(context, ProductActivity::class.java)
                intent.putExtra(Constants.EXTRA_KEY_PRODUCT, itemClicked)
                intent.putExtra(Constants.EXTRA_KEY_IN_FAVORITE, true)
                startActivity(intent)
            }

            override fun onAddToFavoritesClicked(position: Int, view: View?) {
                viewmodel.removeProductFromFavorite(products[position].productName, products[position].productCategory)
                products.removeAt(position)
                adapter?.removeItem(position)
                if (products.isEmpty())
                    binding.favoritesIllustration.visibility = View.VISIBLE
                refreshed = true
            }
        })

    }

}