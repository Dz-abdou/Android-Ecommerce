package com.example.eccomercek

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eccomercek.adapters.ProductRvAdapter
import com.example.eccomercek.models.ProductShortcutModel


class FavoritesFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)


        val recyclerView = view.findViewById<RecyclerView>(R.id.favoriteProductsRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

        val adapter =
            ProductRvAdapter(ProductShortcutModel.MockList.getModel() as ArrayList<ProductShortcutModel>)

        recyclerView.adapter= adapter

        return view
    }


}