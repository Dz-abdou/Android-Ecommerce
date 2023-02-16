package com.example.ecommerce.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.data.models.ProductModel
import com.example.ecommerce.data.repositories.Resource
import com.example.ecommerce.databinding.FragmentHomeBinding
import com.example.ecommerce.ui.activities.ProductActivity
import com.example.ecommerce.ui.adapters.ProductRvAdapter
import com.example.ecommerce.ui.viewModels.ProductsViewModel
import com.example.ecommerce.util.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var viewmodel: ProductsViewModel
    private lateinit var binding: FragmentHomeBinding
    private var products: ArrayList<ProductModel> = ArrayList()
    private var productsFavoriteIDs: ArrayList<String> = ArrayList()
    private var categories: ArrayList<String> = ArrayList()
    private lateinit var rvAdapter: ProductRvAdapter
    private var selectedCategory = ""
    private var searchCriteria = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewmodel = ViewModelProvider(requireActivity())[ProductsViewModel::class.java]
        setProductsRV()
        setFilterSpinner()
        viewmodel.selectedCategory.observe(viewLifecycleOwner, Observer {
            selectedCategory = it
        })


        viewmodel.categoriesLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Failure -> {
                    Toast.makeText(
                        context,
                        "Failed trying to retrieve categories!",
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
                    categories = it.result as ArrayList<String>
                    if (selectedCategory.isEmpty()) {
                        viewmodel.setSelectedCategory(categories[0])
                        viewmodel.getProductsByCategory(selectedCategory)
                    }
                    setCategoryChips()
                }
                null -> {

                }
            }
        })

        viewmodel.productsLiveData.observe(viewLifecycleOwner, Observer {
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

                }
                is Resource.Success -> {
                    products = it.result as ArrayList<ProductModel>
                    viewmodel.getUserFavoritesIDsByCategory(selectedCategory)
                }
                null -> {

                }
            }
        })

        viewmodel.userFavoritesIDsLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Failure -> {
                    productsFavoriteIDs.clear()
                    rvAdapter.changeCategory(products, productsFavoriteIDs)
                    binding.progressCircular.visibility = View.GONE
                    binding.swipeContainer.isRefreshing = false
                }
                Resource.Loading -> {
                }
                is Resource.Success -> {
                    productsFavoriteIDs = it.result
                    rvAdapter.changeCategory(products, productsFavoriteIDs)
                    binding.progressCircular.visibility = View.GONE
                    binding.swipeContainer.isRefreshing = false
                }
                null -> {}
            }
        })

        binding.filterCV.setOnClickListener {
            FilterDialogFragment.newInstance().show(childFragmentManager, "bla");
        }

        binding.searchIconIv.setOnClickListener {
            if(searchCriteria.isEmpty()) {
                showSnackBar(binding.root, "Select a criteria first")
            } else {
                val searchText: String = binding.searchET.text.toString()
                if(searchText.isEmpty()) {
                    showSnackBar(binding.root, "Type something first")
                } else {
                    viewmodel.searchQuery[Constants.CRITERIA] = searchCriteria
                    viewmodel.searchQuery[Constants.SEARCH_VALUE] = searchText.lowercase(Locale.ROOT)
                    viewmodel.searchQuery[Constants.CATEGORIES] = categories
                    findNavController().navigate(R.id.action_homeFragment_to_searchResultFragment)
                }
            }
        }

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
                    viewmodel.addProductToFavorites(
                        products[position].productName,
                        selectedCategory
                    )
                } else {
                    favoriteButton.setImageResource(R.drawable.ic_heart_white)
                    viewmodel.removeProductFromFavorite(
                        products[position].productName,
                        selectedCategory
                    )
                }
            }
        })

        binding.swipeContainer.setOnRefreshListener {
            viewmodel.getProductsByCategory(selectedCategory)
        }
        binding.swipeContainer.setColorSchemeResources(R.color.accent);


    }

//    fun showPopup(view: View, position: Int) {
//        val popup = PopupMenu(requireContext(), view)
//        val inflater = popup.menuInflater
//        inflater.inflate(R.menu.main_menu, popup.menu)
////        popup.setOnMenuItemClickListener { item: MenuItem ->
////            when (item.itemId) {
////                R.id.edit -> {
////                    val connectionToEdit: ConnectionEntity = allConnections.get(position)
////                    showAddConnectionDialog(connectionToEdit)
////                    return@setOnMenuItemClickListener true
////                }
////                R.id.delete -> {
////                    val connectionToDelete: java.util.ArrayList<ConnectionEntity> =
////                        java.util.ArrayList<ConnectionEntity>()
////                    connectionToDelete.add(allConnections.get(position))
////                    viewModel.deleteConnection(connectionToDelete)
////                    return@setOnMenuItemClickListener true
////                }
////                else -> return@setOnMenuItemClickListener false
////            }
////        }
//        popup.show()
//    }

    private fun setCategoryChips() {
        for (category in categories) {
            val chip = Chip(context)
            val chipDrawable = context?.let {
                ChipDrawable.createFromAttributes(
                    it, null, 0,
                    R.style.CustomChipChoice
                )
            }
            chipDrawable?.let { chip.setChipDrawable(it) }
            chip.text = category
            val padding =
                resources.getDimension(R.dimen.chip_padding) / resources.displayMetrics.density
            chip.chipStartPadding = padding
            chip.chipEndPadding = padding
            chip.isClickable = true
            chip.isCheckable = true
            chip.isFocusable = true
            chip.setTextAppearance(R.style.AppTheme_ChipText)
            chip.setTextColor(resources.getColor(R.color.text_gray))

            if (selectedCategory.isNotEmpty() && categories.isNotEmpty()) {
                if (chip.text.toString().trim() == selectedCategory) {
                    chip.isChecked = true
                    chip.setTextColor(resources.getColor(R.color.bg_black))
                }
            } else if (categories.indexOf(category) == 0) {
                chip.isChecked = true
                chip.setTextColor(resources.getColor(R.color.bg_black))
            }

            chip.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    compoundButton.setTextColor(resources.getColor(R.color.bg_black))
                    compoundButton.isClickable = false
                    val newCategory = chip.text.toString().trim()
                    viewmodel.setSelectedCategory(newCategory)
                    viewmodel.getProductsByCategory(selectedCategory)
                } else {
                    compoundButton.setTextColor(resources.getColor(R.color.text_gray))
                    compoundButton.isClickable = true
                }
            }
            binding.categoriesChipGroup.addView(chip)
        }
    }

    private fun setFilterSpinner() {
        val filterByList = arrayOf("Criteria" ,"By name", "By brand")

        val arrayAdapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
            requireContext(), R.layout.spinner_item, filterByList
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
        binding.filterBySpinner.adapter = arrayAdapter

        binding.filterBySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItemText = parent.getItemAtPosition(position) as String
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    activity?.findViewById<TextView>(R.id.sexSpinnerTV)?.setTextColor(resources.getColor(R.color.text_gray))
                }
                when (position) {
                    1 -> {
                        searchCriteria = Constants.PRODUCT_NAME
                    }
                    2 -> {
                        searchCriteria = Constants.PRODUCT_BRAND
                    }

                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }



}