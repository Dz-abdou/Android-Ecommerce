package com.example.ecommerce.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.data.models.OrderModel
import com.example.ecommerce.data.models.ProductInCartModel
import com.google.android.material.button.MaterialButton

class OrderDetailsRvAdapter(private val context: Context, private val products: ArrayList<ProductInCartModel>) :
    RecyclerView.Adapter<OrderDetailsRvAdapter.ModelViewHolder>() {

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onDetailsClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModelViewHolder {

        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.order_detail_item, parent, false)
        return ModelViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        val product = products[position]
        holder.productNameTV.text = product.productName
        holder.productSizeTV.text = product.productSize
        holder.productColorCV.setCardBackgroundColor(Color.parseColor(product.productColor))
        holder.numberOfProductsTV.text = product.productQuantity.toString()
        holder.priceTV.text = "$${product.productPrice}"
        Glide.with(context)
            .load(product.productImage)
            .placeholder(R.drawable.placeholder)
            .centerCrop() // scale to fill the ImageView and crop any extra
            .into(holder.productImageTV)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ModelViewHolder(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {

        val productNameTV :TextView
        val productSizeTV :TextView
        val productImageTV :ImageView
        val productColorCV :CardView
        val numberOfProductsTV :TextView
        val priceTV :TextView

        init {
            productNameTV = itemView.findViewById(R.id.productNameTV) as TextView
            productSizeTV = itemView.findViewById(R.id.sizeTv) as TextView
            productColorCV = itemView.findViewById(R.id.colorCV) as CardView
            productImageTV = itemView.findViewById(R.id.productImageIV) as ImageView
            numberOfProductsTV = itemView.findViewById(R.id.numberOfProductsTV) as TextView
            priceTV = itemView.findViewById(R.id.PriceTv) as TextView

        }

    }



}


