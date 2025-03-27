package com.projects.sydnyrestaurant

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.projects.sydnyrestaurant.models.TableEntity

class TableAdapter(
    private var tables: List<TableEntity>,
    private val onTableClick: (TableEntity) -> Unit
) : BaseAdapter() {

    private var selectedPosition: Int = -1

    override fun getCount(): Int = tables.size

    override fun getItem(position: Int): TableEntity = tables[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.table_item, parent, false)
        val table = getItem(position)

        val tableNumberTextView = view.findViewById<TextView>(R.id.table_number)
        val tableCardView = view.findViewById<MaterialCardView>(R.id.table_card)
        val tableImageView = view.findViewById<ImageView>(R.id.table_img)

        // Set the table number
        tableNumberTextView.text = "${table.tableNumber}"

        // Set the table image
        tableImageView.setImageResource(table.imageResId)

        // Set color based on availability
        if (table.isAvailable) {
            tableCardView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.background))
        } else {
            tableCardView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.gray))
        }

        // Highlight the selected table
        if (position == selectedPosition) {
            tableCardView.setStrokeColor(ContextCompat.getColor(view.context, R.color.orange))
            tableCardView.strokeWidth = 5
        } else {
            tableCardView.setStrokeColor(ContextCompat.getColor(view.context, R.color.white))
            tableCardView.strokeWidth = 0
        }

        // Set click listener for the table
        view.setOnClickListener {
            if (table.isAvailable) { // Only allow clicks on available tables
                selectedPosition = position
                notifyDataSetChanged()
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