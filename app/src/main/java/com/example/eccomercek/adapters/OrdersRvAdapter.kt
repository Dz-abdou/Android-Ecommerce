package com.example.eccomercek.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eccomercek.R
import com.example.eccomercek.models.OrderModel
import com.google.android.material.card.MaterialCardView
import org.w3c.dom.Text

class OrdersRvAdapter(private val orders: ArrayList<OrderModel>, private var processingRv: Boolean) :
    RecyclerView.Adapter<OrdersRvAdapter.ModelViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrdersRvAdapter.ModelViewHolder {

        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return ModelViewHolder(v)
    }

    override fun onBindViewHolder(holder: OrdersRvAdapter.ModelViewHolder, position: Int) {

        holder.orderNumberTv.text = orders[position].orderNumber.toString()
        holder.orderDateTv.text = orders[position].orderDate.toString()
        holder.trackingCodeTv.text = orders[position].trackingCode
        holder.quantityTv.text = orders[position].quantity.toString()
        holder.totalAmountTv.text = orders[position].totalAmount.toString()

        if (processingRv) {
            holder.statusTv.setTextColor(Color.parseColor("#FF7F00"))
            holder.statusTv.text = "Processing"
        }


    }

    override fun getItemCount(): Int {
        return orders.size
    }

    inner class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val orderNumberTv: TextView
        val orderDateTv: TextView
        val trackingCodeTv: TextView
        val quantityTv: TextView
        val totalAmountTv: TextView
        val statusTv: TextView

        init {
            orderNumberTv = itemView.findViewById(R.id.orderNumberTV) as TextView
            orderDateTv = itemView.findViewById(R.id.orderDateTv) as TextView
            trackingCodeTv = itemView.findViewById(R.id.trackingNumberTV) as TextView
            quantityTv = itemView.findViewById(R.id.QuantityTV) as TextView
            totalAmountTv = itemView.findViewById(R.id.PriceTv) as TextView
            statusTv = itemView.findViewById(R.id.statusTV) as TextView

        }

    }



}


