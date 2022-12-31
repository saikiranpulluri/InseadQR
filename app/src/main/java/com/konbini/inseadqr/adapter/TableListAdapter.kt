package com.konbini.inseadqr.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.konbini.inseadqr.R
import com.konbini.inseadqr.data.TableData

class TableListAdapter(
    private val items: ArrayList<TableData>,
    private val onTableSelected: OnTableSelectedListener,
    private val context: Context
) : RecyclerView.Adapter<TableListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tableId: AppCompatTextView = view.findViewById(R.id.table_id)
        val tableView: ConstraintLayout = view.findViewById(R.id.table_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_table, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tableId.text = String.format(item.label)
        if (item.status == "active") {
            //holder.itemView.isClickable = false
            holder.tableView.background = getDrawable(context, R.drawable.round_border_custom_disabled)
            holder.tableId.setTextColor(getColor(context, R.color.white))
        } else if (item.status == "inactive") {
            //holder.itemView.isClickable = true
            holder.tableView.background = getDrawable(context, R.drawable.round_border)
            holder.tableId.setTextColor(getColor(context, R.color.black))
        }

        if (item.isSelected) {
            holder.tableView.background = getDrawable(context, R.drawable.round_border_custom)
            holder.tableId.setTextColor(getColor(context, R.color.white))
        } /*else {
            holder.tableView.background = getDrawable(context, R.drawable.round_border)
            holder.tableId.setTextColor(getColor(context, R.color.black))
        }*/
        holder.itemView.setOnClickListener {
            item.isSelected = !item.isSelected
            onTableSelected.onTableItemSelected(item.id, item.isSelected)
            items.forEach {
                if (item != it) {
                    it.isSelected = false
                }
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnTableSelectedListener {
        fun onTableItemSelected(id: String, selected: Boolean)
    }
}