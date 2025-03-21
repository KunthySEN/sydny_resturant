package com.projects.sydnyrestaurant.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.projects.sydnyrestaurant.models.BookingEntity
import com.projects.sydnyrestaurant.models.TableEntity
import com.projects.sydnyrestaurant.models.UserEntity

@Database(entities = [UserEntity::class, TableEntity::class, BookingEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tableDao(): TableDao
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "restaurant_booking_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}