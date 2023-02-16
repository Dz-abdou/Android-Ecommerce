package com.example.ecommerce.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R

class SizeRvAdapter(private val context: Context, private var sizeList: ArrayList<String>, private var rowIndex: Int = 0) :
    RecyclerView.Adapter<SizeRvAdapter.ModelViewHolder>() {

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
            LayoutInflater.from(parent.context).inflate(R.layout.size_item, parent, false)
        return ModelViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {

        holder.sizeTv.text = sizeList[position]

        holder.sizeItemRL.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
        }
        if (rowIndex == position) {
            holder.sizeTv.setTextColor(Color.parseColor("#111111"))
            holder.sizeCircleIv.setImageResource(R.drawable.ic_circle_filled)
        } else {
            holder.sizeTv.setTextColor(Color.parseColor("#f8f8f8"))
            holder.sizeCircleIv.setImageResource(R.drawable.ic_circle_outlined)
        }

    }

    override fun getItemCount(): Int {
        return sizeList.size
    }

    inner class ModelViewHolder(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {

        var sizeTv: TextView
        var sizeCircleIv: ImageView
        var sizeItemRL: RelativeLayout

        init {
            sizeTv = itemView.findViewById(R.id.sizeTv) as TextView
            sizeCircleIv = itemView.findViewById(R.id.sizeCircle) as ImageView
            sizeItemRL = itemView.findViewById(R.id.sizeItemRL) as RelativeLayout

            sizeItemRL.setOnClickListener(View.OnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(context, position)
                }
            })
        }

    }


}


