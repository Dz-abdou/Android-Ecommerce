package com.example.eccomercek

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eccomercek.adapters.OrdersRvAdapter
import com.example.eccomercek.databinding.FragmentOrdersBinding
import com.example.eccomercek.models.OrderModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup


class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)

        setOrderChips(binding.orderChips)

        val deliveredOrdersRv = binding.deliveredOrdersRV
        deliveredOrdersRv.layoutManager = LinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false)

        val deliveredOrdersAdapter =
            OrdersRvAdapter(OrderModel.MockList.getModel() as ArrayList<OrderModel>, false)

        deliveredOrdersRv.adapter = deliveredOrdersAdapter

        val processingOrdersRv = binding.processingOrdersRV
        processingOrdersRv.layoutManager = LinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false)

        val processingOrdersAdapter =
            OrdersRvAdapter(OrderModel.MockList.getModel() as ArrayList<OrderModel>, true)

        processingOrdersRv.adapter = processingOrdersAdapter
        //make processing Rv out of the screen
        binding.processingOrdersRV.translationX = resources.displayMetrics.widthPixels
            .toFloat()


        return binding.root
    }

    fun setOrderChips(chips: ChipGroup) {

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


                    //animate delivered Rv out of the screen
                    binding.deliveredOrdersRV.animate().translationX(-resources.displayMetrics.widthPixels
                        .toFloat())
                    //binding.deliveredOrdersRV.visibility = View.GONE

                    //animate processing Rv to inside of the screen
                    //binding.processingOrdersRV.visibility = View.VISIBLE
                    binding.processingOrdersRV.animate().translationX(0F)

                }

                if (chip.text == "Processing") {


                    //animate processing Rv out of the screen
                    binding.processingOrdersRV.animate().translationX(resources.displayMetrics.widthPixels
                        .toFloat())
                    //binding.processingOrdersRV.visibility = View.GONE

                    //animate delivered Rv to inside of the screen
                    //binding.deliveredOrdersRV.visibility = View.VISIBLE
                    binding.deliveredOrdersRV.animate().translationX(0F)

                }

            }
            chips.addView(chip)
        }
    }
}