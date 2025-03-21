package com.projects.sydnyrestaurant.repository

import com.projects.sydnyrestaurant.data.BookingDao
import com.projects.sydnyrestaurant.data.TableDao
import com.projects.sydnyrestaurant.models.BookingEntity
import com.projects.sydnyrestaurant.models.TableEntity

class BookingRepository(private val bookingDao: BookingDao, private val tableDao: TableDao) {


    suspend fun getAvailableTables(date: String, startTime: String): List<TableEntity> {

        val endTime = calculateEndTime(startTime)
        val bookings = bookingDao.getBookingsByDateAndTime(date, startTime, endTime)
        val bookedTableIds = bookings.map { it.tableId }

        val allTables = tableDao.getAllTables()
        return allTables.map { table ->
            table.copy(isAvailable = !bookedTableIds.contains(table.id))
        }
    }

    private fun calculateEndTime(startTime: String): String {
        // Implement logic to calculate end time (2 hours later)
        // For simplicity, let's assume startTime is in "HH:mm" format
        val parts = startTime.split(":").map { it.toInt() }
        val hour = parts[0] + 2 // Add 2 hours
        val minute = parts[1]
        return String.format("%02d:%02d", hour % 24, minute) // Wrap around if hour exceeds 24
    }

    suspend fun bookTable(booking: BookingEntity) {
        bookingDao.insertBooking(booking)
        val table = tableDao.getTableById(booking.tableId)
        tableDao.updateTable(table.copy(isAvailable = false)) // Mark table as unavailable
    }
}