package com.example.eccomercek.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eccomercek.R
import com.example.eccomercek.models.ProductShortcutModel

class ProductRvAdapter(private val productList: ArrayList<ProductShortcutModel>) :
    RecyclerView.Adapter<ProductRvAdapter.ModelViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductRvAdapter.ModelViewHolder {

        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.product_item_, parent, false)
        return ModelViewHolder(v)
    }

    override fun onBindViewHolder(holder: ProductRvAdapter.ModelViewHolder, position: Int) {

        holder.productNameTv.text = productList[position].productName
        holder.productPriceTv.text = "$" + productList[position].productPrice
        holder.productIv.setImageResource(productList[position].productImage)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productNameTv: TextView
        var productPriceTv: TextView
        var productIv: ImageView


        init {
            productNameTv =
                itemView.findViewById(R.id.productName) as TextView
            productPriceTv = itemView.findViewById(R.id.productPrice) as TextView
            productIv = itemView.findViewById(R.id.productImageView) as ImageView


        }

    }


}


