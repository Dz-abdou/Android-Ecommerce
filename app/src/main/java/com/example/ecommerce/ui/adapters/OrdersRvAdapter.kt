package com.example.ecommerce.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.data.models.OrderModel
import com.google.android.material.button.MaterialButton

class OrdersRvAdapter(private val orders: ArrayList<OrderModel>, private var processingRv: Boolean) :
    RecyclerView.Adapter<OrdersRvAdapter.ModelViewHolder>() {

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
            LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return ModelViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {

        holder.orderNumberTv.text = orders[position].orderID.split("-")[1]
        val date = orders[position].orderDate.toDate().toLocaleString().split(" ")
        holder.orderDateTv.text = date[0] + " " + date[1] + " " + date[2]
        holder.quantityTv.text = orders[position].numberOfProducts.toString()
        holder.totalAmountTv.text = orders[position].totalAmount.toString()
        holder.statusTv.text = orders[position].status

        if (processingRv) {
            holder.statusTv.setTextColor(Color.parseColor("#FF7F00"))
            holder.trackingCodeTv.text = "-"
        } else {
            holder.trackingCodeTv.text = orders[position].trackingCode
        }


    }

    override fun getItemCount(): Int {
        return orders.size
    }

    inner class ModelViewHolder(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {

        val orderNumberTv: TextView
        val orderDateTv: TextView
        val trackingCodeTv: TextView
        val quantityTv: TextView
        val totalAmountTv: TextView
        val statusTv: TextView
        val detailsButton: MaterialButton

        init {
            orderNumberTv = itemView.findViewById(R.id.orderNumberTV) as TextView
            orderDateTv = itemView.findViewById(R.id.orderDateTv) as TextView
            trackingCodeTv = itemView.findViewById(R.id.trackingNumberTV) as TextView
            quantityTv = itemView.findViewById(R.id.QuantityTV) as TextView
            totalAmountTv = itemView.findViewById(R.id.PriceTv) as TextView
            statusTv = itemView.findViewById(R.id.statusTV) as TextView
            detailsButton = itemView.findViewById(R.id.detailsButton)

            detailsButton.setOnClickListener(View.OnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onDetailsClick(position)
                }
            })
        }

    }



}


