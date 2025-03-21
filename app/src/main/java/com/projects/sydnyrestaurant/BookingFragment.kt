package com.projects.sydnyrestaurant

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.projects.sydnyrestaurant.data.AppDatabase
import com.projects.sydnyrestaurant.data.BookingDao
import com.projects.sydnyrestaurant.models.BookingEntity
import com.projects.sydnyrestaurant.repository.BookingRepository
import com.projects.sydnyrestaurant.repository.BookingViewModelFactory
import com.projects.sydnyrestaurant.viewmodel.BookingViewModel
import java.util.Calendar

class BookingFragment : Fragment() {

    private lateinit var viewModel: BookingViewModel
    private var tableId: Long = 0
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String

    private lateinit var dateTextView: MaterialButton
    private lateinit var timeTextView: MaterialButton
    private lateinit var selectTable: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_booking, container, false)
        val database = AppDatabase.getDatabase(requireContext())
        // Initialize ViewModel with the factory
        val repository = BookingRepository(database.bookingDao(), database.tableDao())
        val factory = BookingViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(BookingViewModel::class.java)

        // Get selected table ID from arguments
        arguments?.let {
            tableId = it.getLong("tableId")
        }

        // Initialize UI components
        dateTextView = view.findViewById(R.id.buttonSelectDate)
        timeTextView = view.findViewById(R.id.buttonSelectTime)
        selectTable = view.findViewById(R.id.select_table)

        // Set up date and time selection
        dateTextView.setOnClickListener { showDatePickerDialog() }
        timeTextView.setOnClickListener { showTimePickerDialog() }

        selectTable.setOnClickListener {

//            if (::selectedDate.isInitialized && ::selectedTime.isInitialized) {
//                // Create a BookingEntity
//                val booking = BookingEntity(
//                    tableId = tableId,
//                    bookingDate = selectedDate,
//                    startTime = selectedTime,
//                    endTime = calculateEndTime(selectedTime) // Calculate end time
//                )
//                viewModel.bookTable(booking) // Book the table
//                // Navigate to confirmation or detail page
//            } else {
//                // Show a message to select date and time
//                // You can use a Toast or Snackbar to inform the user
//            }
            val tableFragment = TableFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, tableFragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate =
                    String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                dateTextView.text = selectedDate
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog =
            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                timeTextView.text = selectedTime
            }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun calculateEndTime(startTime: String): String {
        // Assuming startTime is in "HH:mm" format
        val parts = startTime.split(":").map { it.toInt() }
        val hour = (parts[0] + 2) % 24 // Add 2 hours and wrap around if necessary
        val minute = parts[1]
        return String.format("%02d:%02d", hour, minute)
    }
}