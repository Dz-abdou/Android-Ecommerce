package com.example.eccomercek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eccomercek.adapters.ColorRvAdapter
import com.example.eccomercek.adapters.SizeRvAdapter
import com.example.eccomercek.databinding.ActivityProductBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheetBehavior = BottomSheetBehavior.from<View>(binding.sheet)
        val peekHeight =
            (resources.getDimension(R.dimen.sheet_peek_height) / resources.displayMetrics.density).toInt()
        bottomSheetBehavior.peekHeight = peekHeight
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val sizes: ArrayList<String> = ArrayList()
        sizes.add("S")
        sizes.add("M")
        sizes.add("L")

        val sizeRv = binding.sizesRv
        val sizeRvAdapter = SizeRvAdapter(sizes)

        sizeRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        sizeRv.adapter = sizeRvAdapter

        val colors: ArrayList<String> = ArrayList()
        colors.add("#d7fd71")
        colors.add("#FFBB86FC")
        colors.add("#f8f8f8")

        val colorRv = binding.colorsRv
        val colorRvAdapter = ColorRvAdapter(colors)

        colorRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        colorRv.adapter = colorRvAdapter

    }
}