package com.projects.sydnyrestaurant.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.projects.sydnyrestaurant.models.BookingEntity

@Dao
interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("SELECT * FROM bookings WHERE bookingDate = :date AND startTime < :endTime AND endTime > :startTime")
    suspend fun getBookingsByDateAndTime(date: String, startTime: String, endTime: String): List<BookingEntity>
}