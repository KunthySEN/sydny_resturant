package com.projects.sydnyrestaurant

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.projects.sydnyrestaurant.viewmodel.SharedViewModel
import java.util.Calendar

class BookingFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var dateTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var selectTableButton: MaterialButton

    private var selectedDate: String = ""
    private var selectedTime: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_booking, container, false)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        dateTextView = view.findViewById(R.id.textViewSelectedDate)
        timeTextView = view.findViewById(R.id.textViewSelectedTime)
        selectTableButton = view.findViewById(R.id.select_table)

        view.findViewById<MaterialButton>(R.id.buttonSelectDate).setOnClickListener { showDatePickerDialog() }
        view.findViewById<MaterialButton>(R.id.buttonSelectTime).setOnClickListener { showTimePickerDialog() }

        selectTableButton.setOnClickListener {
            // Set values in SharedViewModel
            sharedViewModel.setSelectedDate(selectedDate)
            sharedViewModel.setSelectedTime(selectedTime)

            // Navigate to TableFragment
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

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            dateTextView.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            timeTextView.text = selectedTime
        }, hour, minute, true)

        timePickerDialog.show()
    }
}