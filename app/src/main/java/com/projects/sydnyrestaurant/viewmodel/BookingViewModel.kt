package com.projects.sydnyrestaurant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.sydnyrestaurant.models.BookingEntity
import com.projects.sydnyrestaurant.models.TableEntity
import com.projects.sydnyrestaurant.repository.BookingRepository
import kotlinx.coroutines.launch

class BookingViewModel(private val repository: BookingRepository) : ViewModel() {

    // LiveData to hold the list of available tables
    private val _availableTables = MutableLiveData<List<TableEntity>>()
    val availableTables: LiveData<List<TableEntity>> get() = _availableTables

    // Load available tables based on the selected date and time
    fun loadAvailableTables(date: String, startTime: String) {
        viewModelScope.launch {
            _availableTables.value = repository.getAvailableTables(date, startTime)
        }
    }

    // Book a table for the user
    fun bookTable(booking: BookingEntity) {
        viewModelScope.launch {
            // Call the repository to save the booking
            repository.bookTable(booking)
        }
    }

    // Helper function to calculate the end time based on the start time
    private fun calculateEndTime(startTime: String): String {
        // Assuming startTime is in "HH:mm" format
        val parts = startTime.split(":").map { it.toInt() }
        val hour = (parts[0] + 2) % 24 // Add 2 hours and wrap around if necessary
        val minute = parts[1]
        return String.format("%02d:%02d", hour, minute)
    }
}