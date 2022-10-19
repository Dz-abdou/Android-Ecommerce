package com.example.eccomercek.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eccomercek.R
import com.example.eccomercek.models.CartModel

class CartRvAdapter(private val cartList: ArrayList<CartModel>) :
    RecyclerView.Adapter<CartRvAdapter.ModelViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartRvAdapter.ModelViewHolder {

        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return ModelViewHolder(v)
    }

    override fun onBindViewHolder(holder: CartRvAdapter.ModelViewHolder, position: Int) {
        holder.productIv.setImageResource(cartList[position].productImage)
        holder.productNameTv.text = cartList[position].productName
        holder.productSizeTv.text = cartList[position].productSize
        holder.productPriceTv.text = "$" + cartList[position].productPrice
        holder.productQuantityTv.text = cartList[position].productQuantity.toString()

    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    inner class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var productIv: ImageView
        var productNameTv: TextView
        var productPriceTv: TextView
        var productSizeTv: TextView
        var productQuantityTv: TextView


        init {
            productIv = itemView.findViewById(R.id.cartProductIV) as ImageView
            productNameTv = itemView.findViewById(R.id.cartProductNameTV) as TextView
            productPriceTv = itemView.findViewById(R.id.cartProductPriceTV) as TextView
            productSizeTv = itemView.findViewById(R.id.cartProductSizeTV) as TextView
            productQuantityTv = itemView.findViewById(R.id.productsNumberTV) as TextView
        }

    }


}


