package com.example.eccomercek.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.eccomercek.R
import com.google.android.material.card.MaterialCardView

class ColorRvAdapter(private val colorList: ArrayList<String>, private var rowIndex: Int = 0) :
    RecyclerView.Adapter<ColorRvAdapter.ModelViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ColorRvAdapter.ModelViewHolder {

        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.color_item, parent, false)
        return ModelViewHolder(v)
    }

    override fun onBindViewHolder(holder: ColorRvAdapter.ModelViewHolder, position: Int) {

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

    inner class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var colorItemRL: RelativeLayout
        var outerCV: MaterialCardView
        var innerCV: MaterialCardView

        init {
            colorItemRL = itemView.findViewById(R.id.colorItemRL) as RelativeLayout
            outerCV = itemView.findViewById(R.id.colorItemOuterCardView) as MaterialCardView
            innerCV = itemView.findViewById(R.id.colorItemInnerCardView) as MaterialCardView
        }

    }



}


