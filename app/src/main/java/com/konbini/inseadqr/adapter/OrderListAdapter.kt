package com.konbini.inseadqr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.konbini.inseadqr.R
import com.konbini.inseadqr.data.CustomerOrdersByStatusModelItem
import com.konbini.inseadqr.data.LineItem

class OrderListAdapter(
    private val isForOrderStatus: Boolean,
    private val items: ArrayList<CustomerOrdersByStatusModelItem>,
    private val onOrderSelectionChanged: OnOrderSelectionChanged
) :
    RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {
    val selectedOrders: ArrayList<CustomerOrdersByStatusModelItem> = arrayListOf()
    var total: Double = 0.0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderId: AppCompatTextView = view.findViewById(R.id.order_id)
        val totalCost: AppCompatTextView = view.findViewById(R.id.total_cost)
        val orderItemsRecyclerView: RecyclerView = view.findViewById(R.id.order_items)
        val subItem: ConstraintLayout = view.findViewById(R.id.sub_item)
        val dropDown: AppCompatImageView = view.findViewById(R.id.drop_down)
        val selectOrder: AppCompatCheckBox = view.findViewById(R.id.select_order)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_order_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.totalCost.text = String.format("$" + item.total)
        holder.orderId.text = String.format("Order ID\n#" + item.id.toString())
        ViewCompat.setNestedScrollingEnabled(holder.orderItemsRecyclerView, false)
        val adapter = OrderDetailsAdapter(item.line_items as ArrayList<LineItem>)
        holder.orderItemsRecyclerView.adapter = adapter

        if (isForOrderStatus) {
            holder.selectOrder.visibility = View.INVISIBLE
        } else {
            holder.selectOrder.visibility = View.VISIBLE
        }

        val expanded = item.isExpanded
        holder.subItem.visibility = if (expanded) View.VISIBLE else View.GONE
        holder.dropDown.setOnClickListener {
            val ex: Boolean = item.isExpanded
            item.isExpanded = !ex
            notifyItemChanged(position)
        }
        holder.selectOrder.isChecked = item.isSelected

        holder.selectOrder.setOnClickListener {
            if (holder.selectOrder.isChecked) {
                item.isSelected = true
                selectedOrders.add(item)
                total += item.total.toDouble()
            } else {
                item.isSelected = false
                selectedOrders.remove(item)
                total -= item.total.toDouble()
            }
            onOrderSelectionChanged.onItemSelectionChanged(total)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnOrderSelectionChanged {
        fun onItemSelectionChanged(total: Double)
    }
}