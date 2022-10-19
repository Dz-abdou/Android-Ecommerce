package com.example.eccomercek.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eccomercek.R

class SizeRvAdapter(private var sizeList: ArrayList<String>, private var rowIndex: Int = 0) :
    RecyclerView.Adapter<SizeRvAdapter.ModelViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SizeRvAdapter.ModelViewHolder {

        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.size_item, parent, false)
        return ModelViewHolder(v)
    }

    override fun onBindViewHolder(holder: SizeRvAdapter.ModelViewHolder, position: Int) {

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

    inner class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var sizeTv: TextView
        var sizeCircleIv: ImageView
        var sizeItemRL: RelativeLayout

        init {
            sizeTv = itemView.findViewById(R.id.sizeTv) as TextView
            sizeCircleIv = itemView.findViewById(R.id.sizeCircle) as ImageView
            sizeItemRL = itemView.findViewById(R.id.sizeItemRL) as RelativeLayout
        }

    }


}


