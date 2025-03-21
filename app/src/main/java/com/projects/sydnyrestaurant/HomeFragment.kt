package com.projects.sydnyrestaurant

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.projects.sydnyrestaurant.models.Item
import kotlin.text.Charsets.UTF_8

class HomeFragment : Fragment() {

//    private lateinit var listView: ListView
    private lateinit var gridView: GridView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        gridView = view.findViewById(R.id.grid_view)

        // Load items from JSON file
        val items = loadItemsFromJson(requireContext())

        // Create an adapter and set it to the GridView
        val adapter = ItemAdapter(requireContext(), items)
        gridView.adapter = adapter

        return view
    }

    private fun loadItemsFromJson(context: Context): List<Item> {
        val inputStream = context.assets.open("items.json") // Load JSON from assets
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val json = String(buffer, charset = UTF_8)
        val itemType = object : TypeToken<List<Item>>() {}.type // Define the type for Gson
        return Gson().fromJson(json, itemType) // Parse JSON to List<Item>
    }
}