package com.example.eccomercek.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eccomercek.R
import com.example.eccomercek.models.ShippingAddressModel

class ShippingAddressRvAdapter(private var addressesList: ArrayList<ShippingAddressModel>) :
    RecyclerView.Adapter<ShippingAddressRvAdapter.ModelViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShippingAddressRvAdapter.ModelViewHolder {

        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.shipping_address_item, parent, false)
        return ModelViewHolder(v)
    }

    override fun onBindViewHolder(holder: ShippingAddressRvAdapter.ModelViewHolder, position: Int) {

        holder.nameTv.text = addressesList[position].recieverName
        holder.addressTv.text = addressesList[position].address
        holder.restOfAdressTv.text =
            addressesList[position].city + ", " + addressesList[position].provinceOrStateOrRegion+ " " + addressesList[position].zipCode + ", " + addressesList[position].country

    }

    override fun getItemCount(): Int {
        return addressesList.size
    }

    inner class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nameTv: TextView
        var addressTv: TextView
        var restOfAdressTv: TextView
        var editTv: TextView
        var useCheckBox: CheckBox

        init {
            nameTv = itemView.findViewById(R.id.nameTV) as TextView
            addressTv = itemView.findViewById(R.id.addressTV) as TextView
            restOfAdressTv = itemView.findViewById(R.id.restOfAddressTV) as TextView
            editTv = itemView.findViewById(R.id.editTV) as TextView
            useCheckBox = itemView.findViewById(R.id.useAddressCheckbox) as CheckBox
        }

    }


}


