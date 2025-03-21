package com.projects.sydnyrestaurant

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import com.projects.sydnyrestaurant.data.AppDatabase
import com.projects.sydnyrestaurant.models.TableEntity
import com.projects.sydnyrestaurant.repository.BookingRepository
import com.projects.sydnyrestaurant.repository.BookingViewModelFactory
import com.projects.sydnyrestaurant.viewmodel.BookingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date

class TableFragment : Fragment() {

    private lateinit var tableGridView: GridView
    private lateinit var viewModel: BookingViewModel
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_table, container, false)

        tableGridView = view.findViewById(R.id.table_grid_view)

        database = AppDatabase.getDatabase(requireContext())
        val repository = BookingRepository(database.bookingDao(), database.tableDao())
        val factory = BookingViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(BookingViewModel::class.java)

        val datetimeBooking = "Thu Mar 20 16:58:59 GMT+07:00 2025"
        Log.d("dateTimeBooking", "value=> $datetimeBooking")

        lifecycleScope.launch {
            // Load sample tables
            val tables = database.tableDao().getAllTables()
//            tables.forEach {
//                Log.d("tableList", "value=> ${it.dateTime}")
//                if (it.dateTime.contains(datetimeBooking))
//
//            }
            Log.d("dateTimeBooking", "value=> $tables.")
        // Create an adapter and set it to the Table GridView
            val adapter = TableAdapter(tables) { table ->
                // Handle table click
                // Navigate to BookingFragment with selected table details
                val checkoutFragment = CheckoutFragment().apply {
                    arguments = Bundle().apply {
                        putLong("tableId", table.id)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, checkoutFragment)
                    .addToBackStack(null)
                    .commit()
            }
            tableGridView.adapter = adapter
        }




        return view
    }


}