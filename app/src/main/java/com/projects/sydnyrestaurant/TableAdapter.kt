package com.projects.sydnyrestaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.projects.sydnyrestaurant.models.TableEntity

class TableAdapter(
    private var tables: List<TableEntity>,
    private val onTableClick: (TableEntity) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = tables.size

    override fun getItem(position: Int): TableEntity = tables[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.table_item, parent, false)
        val table = getItem(position)

        val tableNumberTextView = view.findViewById<TextView>(R.id.table_number)
        val tableCardView = view.findViewById<CardView>(R.id.table_card)

        // Set the table number
        tableNumberTextView.text = table.tableNumber

        // Set color based on availability
        tableCardView.setCardBackgroundColor(
            if (table.isAvailable) {
                parent?.context?.getColor(R.color.orange) ?: 0 // Use safe call to get context
            } else {
                parent?.context?.getColor(R.color.gray) ?: 0 // Use safe call to get context
            }
        )

        // Set click listener for the table
        view.setOnClickListener {
            onTableClick(table)
        }

        return view
    }

    fun updateTables(newTables: List<TableEntity>) {
        tables = newTables
        notifyDataSetChanged()
    }
}