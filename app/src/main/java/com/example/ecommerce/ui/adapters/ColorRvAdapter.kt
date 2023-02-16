package com.example.ecommerce.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.google.android.material.card.MaterialCardView

class ColorRvAdapter(private val context: Context, private val colorList: ArrayList<String>, private var rowIndex: Int = 0) :
    RecyclerView.Adapter<ColorRvAdapter.ModelViewHolder>() {

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
            LayoutInflater.from(parent.context).inflate(R.layout.color_item, parent, false)
        return ModelViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {

        holder.outerCV.setCardBackgroundColor(Color.parseColor(colorList[position]))
        holder.innerCV.setCardBackgroundColor(Color.parseColor(colorList[position]))

        holder.colorItemRL.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
        }
        if (rowIndex == position) {
            holder.outerCV.visibility = View.VISIBLE
        } else {
            holder.outerCV.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return colorList.size
    }

    inner class ModelViewHolder(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {

        var colorItemRL: RelativeLayout
        var outerCV: MaterialCardView
        var innerCV: MaterialCardView

        init {
            colorItemRL = itemView.findViewById(R.id.colorItemRL) as RelativeLayout
            outerCV = itemView.findViewById(R.id.colorItemOuterCardView) as MaterialCardView
            innerCV = itemView.findViewById(R.id.colorItemInnerCardView) as MaterialCardView

            colorItemRL.setOnClickListener(View.OnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(context, position)
                }
            })
        }

    }



}


