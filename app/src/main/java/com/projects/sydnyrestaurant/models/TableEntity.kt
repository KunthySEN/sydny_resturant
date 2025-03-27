package com.projects.sydnyrestaurant.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.projects.sydnyrestaurant.utils.Converters
import java.util.Date

@Entity(tableName = "tables")
//@TypeConverters(Converters::class)
data class TableEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tableNumber: String,
    val capacity: Int,
    var isAvailable: Boolean = true ,// Default to true
    val imageResId : Int
)

