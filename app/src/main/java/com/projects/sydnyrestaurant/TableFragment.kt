package com.projects.sydnyrestaurant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.projects.sydnyrestaurant.data.AppDatabase
import com.projects.sydnyrestaurant.models.TableEntity
import com.projects.sydnyrestaurant.repository.BookingRepository
import com.projects.sydnyrestaurant.repository.BookingViewModelFactory
import com.projects.sydnyrestaurant.viewmodel.SharedViewModel
import com.projects.sydnyrestaurant.viewmodel.BookingViewModel
import kotlinx.coroutines.launch

class TableFragment : Fragment() {

    private lateinit var tableGridView: GridView
    private lateinit var reserveButton: Button
    private lateinit var viewModel: BookingViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var database: AppDatabase

    private var selectedTableId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_table, container, false)

        tableGridView = view.findViewById(R.id.table_grid_view)
        reserveButton = view.findViewById(R.id.reserve_button)

        // Initialize the database and repository
        database = AppDatabase.getDatabase(requireContext())
        val repository = BookingRepository(database.bookingDao(), database.tableDao())

        // Initialize ViewModel with the factory
        val factory = BookingViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(BookingViewModel::class.java)

        // Initialize SharedViewModel
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Initially disable the button
        reserveButton.isEnabled = false
        reserveButton.setBackgroundTintList(null) // Set to default or transparent
        reserveButton.setTextColor(resources.getColor(android.R.color.darker_gray, null)) // Set to gray

        // Load tables based on selected date and time
        loadAvailableTables()

        reserveButton.setOnClickListener {
            if (selectedTableId != null) {
                sharedViewModel.setSelectedTableId(selectedTableId!!) // Set in SharedViewModel

                // Navigate to CheckoutFragment
                val checkoutFragment = CheckoutFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, checkoutFragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(requireContext(), "Please select a table", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun loadAvailableTables() {
        val date = sharedViewModel.selectedDate.value
        val time = sharedViewModel.selectedTime.value

        if (date != null && time != null) {
            viewModel.fetchAvailableTables(date, time)

            viewModel.availableTables.observe(viewLifecycleOwner) { tables ->
                Log.d("my_table", "value: => $tables")

                val adapter = TableAdapter(tables) { table ->
                    selectedTableId = table.id // Store selected table ID
                    reserveButton.isEnabled = true // Enable the Reserve button
                    reserveButton.setBackgroundTintList(resources.getColorStateList(R.color.orange, null)) // Set to orange
                    reserveButton.setTextColor(resources.getColor(android.R.color.white, null)) // Set to white
                }
                tableGridView.adapter = adapter
            }
        } else {
            Toast.makeText(requireContext(), "Please select a date and time first.", Toast.LENGTH_SHORT).show()
        }
    }
}