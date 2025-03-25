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

    private val _availableTables = MutableLiveData<List<TableEntity>>()
    val availableTables: LiveData<List<TableEntity>> get() = _availableTables

    fun fetchAvailableTables(selectedDate: String, selectedTime: String) {
        viewModelScope.launch {
            val tables = repository.getAvailableTables(selectedDate, selectedTime)
            _availableTables.postValue(tables)
        }
    }

    fun bookTable(booking: BookingEntity) {
        viewModelScope.launch {
            if (!repository.isTableBooked(booking.tableId, booking.bookingDate, booking.startTime)) {
                repository.bookTable(booking)
            } else {
                // Handle the case where the table is already booked
            }
        }
    }
}