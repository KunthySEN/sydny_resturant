package com.projects.sydnyrestaurant.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    val operatingHours: OperatingHours,
    val tables: List<Table>
)
@Serializable
data class OperatingHours(
    val open: String,
    val close: String
)
@Serializable
data class Table(
    val tableNumber: String,
    val image: String,
    val isAvailable: Boolean,
    val bookings: ArrayList<Booking>
)
@Serializable
data class Booking(
    val date: String,
    val startTime: String,
    val endTime: String
)

data class RestaurantData( @SerializedName("restaurant") val restaurant: Restaurant )
