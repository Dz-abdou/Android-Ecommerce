package com.example.ecommerce.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R

class ProductImagesRvAdapter(private val context: Context,private var imagesList: ArrayList<String>) :
    RecyclerView.Adapter<ProductImagesRvAdapter.ModelViewHolder>() {

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(context: Context, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModelViewHolder {

        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.product_image_item, parent, false)
        return ModelViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        Glide.with(context)
            .load(imagesList[position])
            .placeholder(R.drawable.placeholder)
            .centerCrop() // scale to fill the ImageView and crop any extra
            .into(holder.productImageItemIV)
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    inner class ModelViewHolder(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {

        val productImageItemIV: ImageView

        init {
            productImageItemIV = itemView.findViewById(R.id.productImageRvIv) as ImageView

            productImageItemIV.setOnClickListener(View.OnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(context, position)
                }
            })
        }

    }


}


