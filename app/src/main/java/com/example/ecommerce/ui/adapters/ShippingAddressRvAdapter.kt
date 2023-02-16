package com.example.ecommerce.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.data.models.ShippingAddressModel

class ShippingAddressRvAdapter(private val context: Context,private var addressesList: ArrayList<ShippingAddressModel>) :
    RecyclerView.Adapter<ShippingAddressRvAdapter.ModelViewHolder>() {

        private var mListener: OnItemClickListener? = null

        interface OnItemClickListener {
            fun onItemClick(context: Context, position: Int)
            fun onUseThisAddressClick(position: Int, checkBox: CheckBox)
        }

        fun setOnItemClickListener(listener: OnItemClickListener?) {
            mListener = listener
        }


        override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModelViewHolder {

        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.shipping_address_item, parent, false)
        return ModelViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        holder.addressTv.text = addressesList[position].address
        holder.restOfAdressTv.text =
            addressesList[position].city + ", " + addressesList[position].provinceOrStateOrRegion+ " " + addressesList[position].zipCode + ", " + addressesList[position].country
        holder.useCheckBox.isChecked = addressesList[position].useThisAddress

    }

    override fun getItemCount(): Int {
        return addressesList.size
    }

    inner class ModelViewHolder(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {

        var addressTv: TextView
        var restOfAdressTv: TextView
        var editTv: TextView
        var useCheckBox: CheckBox

        init {
            addressTv = itemView.findViewById(R.id.addressTV) as TextView
            restOfAdressTv = itemView.findViewById(R.id.restOfAddressTV) as TextView
            editTv = itemView.findViewById(R.id.editTV) as TextView
            useCheckBox = itemView.findViewById(R.id.useAddressCheckbox) as CheckBox

            itemView.setOnClickListener(View.OnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(context, position)
                }
            })

            useCheckBox.setOnClickListener(View.OnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onUseThisAddressClick(position, useCheckBox)
                }
            })
        }

    }

    fun uncheckCheckbox(position: Int) {
        addressesList[position].useThisAddress = false
        notifyItemChanged(position)
    }

}


