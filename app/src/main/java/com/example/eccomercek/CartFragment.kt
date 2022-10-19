package com.example.eccomercek

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eccomercek.adapters.CartRvAdapter
import com.example.eccomercek.databinding.FragmentCartBinding
import com.example.eccomercek.models.CartModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartFragment : Fragment() {

    private lateinit var binding:FragmentCartBinding
    override fun onStart() {
        super.onStart()

        val bottomNavBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavBar.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCartBinding.inflate(inflater, container, false)
        val recyclerView = binding.cartRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)

        val adapter =
            CartRvAdapter(CartModel.MockList.getModel() as ArrayList<CartModel>)

        recyclerView.adapter= adapter

        binding.checkoutButton.setOnClickListener {
           startActivity(Intent(activity, ProductActivity::class.java))
        }


        return binding.root
    }

}