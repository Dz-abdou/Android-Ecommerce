package com.example.ecommerce.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.data.models.ProductInCartModel

class CartRvAdapter(private val context: Context, private var cartList: ArrayList<ProductInCartModel>) :
    RecyclerView.Adapter<CartRvAdapter.ModelViewHolder>() {

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onIncreaseClick(context: Context, position: Int, view: View)
        fun onDecreaseClick(context: Context, position: Int, view: View)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModelViewHolder {

        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return ModelViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        holder.productNameTv.text = cartList[position].productName
        holder.productSizeTv.text = cartList[position].productSize
        holder.productPriceTv.text = "$" + cartList[position].productPrice
        holder.productQuantityTv.text = cartList[position].productQuantity.toString()
        Glide.with(context)
            .load(cartList[position].productImage)
            .placeholder(R.drawable.placeholder)
            .centerCrop() // scale to fill the ImageView and crop any extra
            .into(holder.productIv)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    inner class ModelViewHolder(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {

        var productIv: ImageView
        var productNameTv: TextView
        var productPriceTv: TextView
        var productSizeTv: TextView
        var productQuantityTv: TextView
        var increaseQuantity: ImageView
        var decreaseQuantity: ImageView


        init {
            productIv = itemView.findViewById(R.id.cartProductIV) as ImageView
            productNameTv = itemView.findViewById(R.id.cartProductNameTV) as TextView
            productPriceTv = itemView.findViewById(R.id.cartProductPriceTV) as TextView
            productSizeTv = itemView.findViewById(R.id.cartProductSizeTV) as TextView
            productQuantityTv = itemView.findViewById(R.id.QuantityTV) as TextView
            increaseQuantity = itemView.findViewById(R.id.increaseCounterIV) as ImageView
            decreaseQuantity = itemView.findViewById(R.id.decreaseCounterIV) as ImageView

            increaseQuantity.setOnClickListener(View.OnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onIncreaseClick(context, position, productQuantityTv)
                }
            })

            decreaseQuantity.setOnClickListener(View.OnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onDecreaseClick(context, position, productQuantityTv)
                }
            })
        }
    }
    fun removeItem(position: Int) {
        cartList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun emptyRv() {
        cartList = ArrayList()
        notifyDataSetChanged()
    }
}