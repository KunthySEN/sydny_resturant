package com.projects.sydnyrestaurant.repository

import com.projects.sydnyrestaurant.data.BookingDao
import com.projects.sydnyrestaurant.data.TableDao
import com.projects.sydnyrestaurant.models.BookingEntity
import com.projects.sydnyrestaurant.models.TableEntity

class BookingRepository(private val bookingDao: BookingDao, private val tableDao: TableDao) {

    // Get available tables based on the selected date and time
    suspend fun getAvailableTables(date: String, startTime: String): List<TableEntity> {
        val endTime = calculateEndTime(startTime)
        val bookings = bookingDao.getBookingsByDateAndTime(date, startTime, endTime)
        val bookedTableIds = bookings.map { it.tableId }

        val allTables = tableDao.getAllTables()
        return allTables.map { table ->
            table.copy(isAvailable = !bookedTableIds.contains(table.id))
        }
    }

    // Calculate the end time based on the start time (e.g., 2 hours later)
    private fun calculateEndTime(startTime: String): String {
        val parts = startTime.split(":").map { it.toInt() }
        val hour = (parts[0] + 2) % 24 // Add 2 hours and wrap around if necessary
        val minute = parts[1]
        return String.format("%02d:%02d", hour, minute)
    }

    // Book a table and mark it as unavailable
    suspend fun bookTable(booking: BookingEntity) {
        bookingDao.insertBooking(booking) // Insert the booking into the database
        val table = tableDao.getTableById(booking.tableId) // Get the table by ID
        tableDao.updateTable(table.copy(isAvailable = false)) // Mark the table as unavailable
    }

    // Check if a table is already booked for the selected date and time
    suspend fun isTableBooked(tableId: Long, date: String, time: String): Boolean {
        val endTime = calculateEndTime(time)
        val bookings = bookingDao.getBookingsByDateAndTime(date, time, endTime)
        return bookings.any { it.tableId == tableId }
    }
}