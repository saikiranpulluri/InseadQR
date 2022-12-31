package com.konbini.inseadqr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.konbini.inseadqr.R
import com.konbini.inseadqr.data.LineItem

class OrderDetailsAdapter(
    private val items: ArrayList<LineItem>
) :
    RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: AppCompatTextView = view.findViewById(R.id.product_name)
        val quantity: AppCompatTextView = view.findViewById(R.id.quantity)
        val cost: AppCompatTextView = view.findViewById(R.id.cost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_order_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.productName.text = String.format(item.name)
        holder.quantity.text = String.format(item.quantity.toString())
        holder.cost.text = String.format("$ ${item.total}")
    }

    override fun getItemCount(): Int {
        return items.size
    }
}