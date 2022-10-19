package com.example.eccomercek

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eccomercek.adapters.ProductRvAdapter
import com.example.eccomercek.models.ProductShortcutModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup

class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.productsRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

        val adapter =
            ProductRvAdapter(ProductShortcutModel.MockList.getModel() as ArrayList<ProductShortcutModel>)

        recyclerView.adapter= adapter

        val chips = view.findViewById<View>(R.id.categoriesChipGroup) as ChipGroup
        val categories:ArrayList<String> = ArrayList()
        categories.add("T-shirts")
        categories.add("Hoodies")
        categories.add("Pants")
        categories.add("Shoes")
        categories.add("Shorts")
        setCategoryChips(categories, chips)


        return view
    }

    fun setCategoryChips(categories: List<String>, chips: ChipGroup){
        for (category in categories) {
            val chip:Chip = Chip(context)
            val chipDrawable = context?.let { ChipDrawable.createFromAttributes(it, null, 0, R.style.CustomChipChoice) }
            chipDrawable?.let { chip.setChipDrawable(it) }
            chip.text = category
            val padding = resources.getDimension(R.dimen.chip_padding) / resources.displayMetrics.density
            chip.chipStartPadding = padding
            chip.chipEndPadding = padding
            chip.isClickable = true
            chip.isCheckable = true
            chip.isFocusable = true
            chip.setTextAppearance(R.style.AppTheme_ChipText)
            chip.setTextColor(resources.getColor(R.color.text_gray))

            if (categories.indexOf(category) == 0) {
                chip.isChecked = true
                chip.setTextColor(resources.getColor(R.color.bg_black))
            }


            chip.setOnCheckedChangeListener { compoundButton, b ->
                if (b)
                    compoundButton.setTextColor(resources.getColor(R.color.bg_black))
                else
                    compoundButton.setTextColor(resources.getColor(R.color.text_gray))
            }
            chips.addView(chip)
        }
    }


}