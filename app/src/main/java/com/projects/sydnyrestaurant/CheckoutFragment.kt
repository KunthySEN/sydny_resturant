package com.projects.sydnyrestaurant

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.projects.sydnyrestaurant.data.AppDatabase
import com.projects.sydnyrestaurant.models.BookingEntity
import com.projects.sydnyrestaurant.repository.BookingRepository
import com.projects.sydnyrestaurant.repository.BookingViewModelFactory
import com.projects.sydnyrestaurant.viewmodel.SharedViewModel
import com.projects.sydnyrestaurant.viewmodel.BookingViewModel

class CheckoutFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var viewModel: BookingViewModel
    private lateinit var dateTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var tableIdTextView: TextView
    private lateinit var finalizeBookingButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_checkout, container, false)

        // Initialize SharedViewModel
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Initialize UI components
        dateTextView = view.findViewById(R.id.textViewDate)
        timeTextView = view.findViewById(R.id.textViewTime)
        tableIdTextView = view.findViewById(R.id.textViewTableId)
        finalizeBookingButton = view.findViewById(R.id.buttonFinalizeBooking)

        // Observe the LiveData from SharedViewModel
        sharedViewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            dateTextView.text = "Date: $date"
        }

        sharedViewModel.selectedTime.observe(viewLifecycleOwner) { time ->
            timeTextView.text = "Time: $time"
        }

        sharedViewModel.selectedTableId.observe(viewLifecycleOwner) { tableId ->
            tableIdTextView.text = "Table ID: $tableId"
        }

        // Initialize the BookingViewModel
        val database = AppDatabase.getDatabase(requireContext())
        val repository = BookingRepository(database.bookingDao(), database.tableDao())
        val factory = BookingViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(BookingViewModel::class.java)

        finalizeBookingButton.setOnClickListener {
            confirmBooking()
        }

        return view
    }

    private fun confirmBooking() {
        val date = sharedViewModel.selectedDate.value
        val time = sharedViewModel.selectedTime.value
        val tableId = sharedViewModel.selectedTableId.value

        if (date != null && time != null && tableId != null) {
            // Create a BookingEntity
            val booking = BookingEntity(
                tableId = tableId,
                bookingDate = date,
                startTime = time,
                endTime = calculateEndTime(time) // Assuming you have a method to calculate end time
            )

            // Call the ViewModel to book the table
            viewModel.bookTable(booking)

            // Navigate to the Thank You activity
            val intent = Intent(requireContext(), ThankYouActivity::class.java)
            startActivity(intent)

            // Optionally navigate to a confirmation screen or back to the main screen
        } else {
            Toast.makeText(requireContext(), "Please select all details before confirming.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateEndTime(startTime: String): String {
        // Assuming startTime is in "HH:mm" format
        val parts = startTime.split(":").map { it.toInt() }
        val hour = (parts[0] + 2) % 24 // Add 2 hours and wrap around if necessary
        val minute = parts[1]
        return String.format("%02d:%02d", hour, minute)
    }
}