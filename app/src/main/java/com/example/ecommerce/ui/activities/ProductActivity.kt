package com.example.ecommerce.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.ui.adapters.ColorRvAdapter
import com.example.ecommerce.ui.adapters.ProductImagesRvAdapter
import com.example.ecommerce.ui.adapters.SizeRvAdapter
import com.example.ecommerce.data.models.ProductInCartModel
import com.example.ecommerce.data.models.ProductModel
import com.example.ecommerce.databinding.ActivityProductBinding
import com.example.ecommerce.util.Constants
import com.example.ecommerce.util.getDateAndTime
import com.example.ecommerce.util.getSerializable
import com.example.ecommerce.ui.viewModels.ProductActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding
    private lateinit var product: ProductModel
    private lateinit var viewModel: ProductActivityViewModel
    private lateinit var selectedSize: String
    private lateinit var selectedColor: String
    private var productInFavorites = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        product = getSerializable(this, Constants.EXTRA_KEY_PRODUCT, ProductModel::class.java)
        productInFavorites = intent.getBooleanExtra(Constants.EXTRA_KEY_IN_FAVORITE, false)

        selectedSize = product.productSizes[0]
        selectedColor = product.productColors[0]

        if (productInFavorites)
            binding.FavoriteIV.setImageResource(R.drawable.ic_heart_black)

        viewModel = ViewModelProvider(this)[ProductActivityViewModel::class.java]

        viewModel.productInFavoritesLiveData.observe(this, Observer {
            productInFavorites = it
        })

        val bottomSheetBehavior = BottomSheetBehavior.from<View>(binding.sheet)
        val peekHeight =
            (resources.getDimension(R.dimen.sheet_peek_height) / resources.displayMetrics.density).toInt()
        bottomSheetBehavior.peekHeight = peekHeight
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        initViews()

        binding.addToFavoritesCV.setOnClickListener(View.OnClickListener {
            if (productInFavorites){
                binding.FavoriteIV.setImageResource(R.drawable.ic_heart_white)
                viewModel.removeProductFromFavorite(product.productName, product.productCategory)
            } else {
                binding.FavoriteIV.setImageResource(R.drawable.ic_heart_black)
                viewModel.addProductToFavorites(product.productName, product.productCategory)
            }
            viewModel.setProductInFavoritesLiveData(productInFavorites)
        })

        binding.addToCartButton.setOnClickListener {
            val productInCartModel = ProductInCartModel(product.productName + getDateAndTime(),product.productImages[0], product.productName,
                selectedSize, product.productPrice, binding.counterTV.text.toString().toInt(), selectedColor, product.productCategory)
            viewModel.addProductToCart(productInCartModel)
            lifecycleScope.launch {
                binding.addToCartButton.isEnabled = false
                delay(3000)
                binding.addToCartButton.isEnabled = true
            }
        }

        binding.increaseCounterIV.setOnClickListener {
            val currentQuantity = binding.counterTV.text.toString().toInt()+1
            if (currentQuantity <= 10)
                binding.counterTV.text = currentQuantity.toString()
            else{
                Toast.makeText(this, "10 is the Maximum number of products per order!", Toast.LENGTH_LONG).show()
                it.isEnabled = false
            }
        }

        binding.decreaseCounterIV.setOnClickListener {
            val currentQuantity = binding.counterTV.text.toString().toInt()-1
            if (currentQuantity >= 1)
                binding.counterTV.text = currentQuantity.toString()
            binding.increaseCounterIV.isEnabled = true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        Glide.with(this)
            .load(product.productImages[0])
            .placeholder(R.drawable.placeholder)
            .centerCrop() // scale to fill the ImageView and crop any extra
            .into(binding.backgroundIV);
        binding.productNameTv.text = product.productName
        binding.productPriceTv.text = "$${product.productPrice}"

        val sizeRvAdapter = SizeRvAdapter(this, product.productSizes as ArrayList<String>)
        binding.sizesRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.sizesRv.adapter = sizeRvAdapter
        sizeRvAdapter.setOnItemClickListener(object: SizeRvAdapter.OnItemClickListener{
            override fun onItemClick(context: Context, position: Int) {
                selectedSize = product.productSizes[position]
            }

        })

        val colorRvAdapter = ColorRvAdapter(this, product.productColors as ArrayList<String>)
        binding.colorsRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.colorsRv.adapter = colorRvAdapter
        colorRvAdapter.setOnItemClickListener(object: ColorRvAdapter.OnItemClickListener{
            override fun onItemClick(context: Context, position: Int) {
                selectedColor = product.productColors[position]
            }
        })

        val imagesRvAdapter =
            ProductImagesRvAdapter(this, product.productImages as ArrayList<String>)
        binding.imagesRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.imagesRv.adapter = imagesRvAdapter
        imagesRvAdapter.setOnItemClickListener(object : ProductImagesRvAdapter.OnItemClickListener {
            override fun onItemClick(context: Context, position: Int) {
                Glide.with(context)
                    .load(product.productImages[position])
                    .placeholder(R.drawable.placeholder)
                    .centerCrop() // scale to fill the ImageView and crop any extra
                    .into(binding.backgroundIV);
            }

        })
    }
}