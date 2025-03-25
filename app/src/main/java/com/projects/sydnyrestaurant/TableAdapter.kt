package com.projects.sydnyrestaurant

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
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
        tableNumberTextView.text = "Table ${table.tableNumber}"

        // Set color based on availability
            if (table.isAvailable) {
                tableCardView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.orange))
            } else {
                tableCardView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.gray))

            }

        // Set click listener for the table
        view.setOnClickListener {
            if (table.isAvailable) { // Only allow clicks on available tables
                onTableClick(table)
            }
        }

        return view
    }

    fun updateTables(newTables: List<TableEntity>) {
        tables = newTables
        notifyDataSetChanged()
    }
}