package com.example.ecommerce.ui.adapters

import android.R.attr.left
import android.R.attr.right
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints.LayoutParams
import androidx.core.view.marginTop
import androidx.core.view.updateMargins
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.data.models.ProductModel


class ProductRvAdapter(private val context:Context, private var productList: ArrayList<ProductModel>, private var productInFavoriteIDs: ArrayList<String>?) :
    RecyclerView.Adapter<ProductRvAdapter.ModelViewHolder>() {

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onAddToFavoritesClicked(position: Int, view: View?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    fun setProductInFavoriteIDs(products: ArrayList<String>) {
        this.productInFavoriteIDs = products
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModelViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.product_item_, parent, false)
        return mListener?.let { ModelViewHolder(v, it) }!!
    }

    override fun onViewDetachedFromWindow(holder: ModelViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.favoriteIV.setImageResource(R.drawable.ic_heart_white)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {

        holder.productNameTv.text = productList[position].productName
        holder.productPriceTv.text = "$" + productList[position].productPrice
        Glide.with(context)
            .load(productList[position].productImages[0])
            .override(1000,1230)
            .placeholder(R.drawable.placeholder)
            .centerCrop() // scale to fill the ImageView and crop any extra
            .into(holder.productIv);
        if (productInFavoriteIDs == null) {
            //Called by Favorites Fragment
            holder.favoriteIV.setImageResource(R.drawable.ic_heart_black)
        } else {
            //Called by Home Fragment
            if (productInFavoriteIDs!!.contains(productList[position].productName))
                holder.favoriteIV.setImageResource(R.drawable.ic_heart_black)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ModelViewHolder(itemView: View,listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var productNameTv: TextView
        var productPriceTv: TextView
        var productIv: ImageView
        var favoriteIV: ImageView

        init {
            productNameTv =
                itemView.findViewById(R.id.productName) as TextView
            productPriceTv = itemView.findViewById(R.id.productPrice) as TextView
            productIv = itemView.findViewById(R.id.productImageView) as ImageView
            favoriteIV = itemView.findViewById(R.id.FavoriteIV)


            itemView.setOnClickListener(View.OnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            })

            favoriteIV.setOnClickListener(View.OnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onAddToFavoritesClicked(position, favoriteIV)
                }
            })
        }

    }

    fun removeItem(position: Int) {
        productInFavoriteIDs?.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun changeCategory(
        newProductsList: ArrayList<ProductModel>,
        productsInFavoriteIDs: ArrayList<String>
    ){
        notifyItemRangeRemoved(0, productList.size)
        this.productList = newProductsList
        this.productInFavoriteIDs = productsInFavoriteIDs
        notifyDataSetChanged()
    }

    fun clear() {
        productList.clear()
        if (productInFavoriteIDs != null)
            productInFavoriteIDs?.clear()
        notifyDataSetChanged()
    }

    fun addAll(products: ArrayList<ProductModel>,  productsInFavoriteIDs: ArrayList<String>) {
        productList.addAll(products)
        this.productInFavoriteIDs?.addAll(productsInFavoriteIDs)

        notifyDataSetChanged()
    }

    fun addAll(products: ArrayList<ProductModel>) {
        productList.addAll(products)
        notifyDataSetChanged()
    }


}


