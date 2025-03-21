package com.projects.sydnyrestaurant.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tableId: Long,
    val bookingDate: String,
    val startTime: String,
    val endTime: String
)

