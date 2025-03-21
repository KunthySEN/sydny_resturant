package com.projects.sydnyrestaurant

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.projects.sydnyrestaurant.data.AppDatabase
import com.projects.sydnyrestaurant.models.TableEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize the Room database
        database = AppDatabase.getDatabase(this)

        // Load the HomeFragment by default
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
            // Insert sample tables if they don't exist
            insertSampleTables()
        }

        // Set up BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.nav_booking -> {
                    loadFragment(BookingFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        // Replace the current fragment with the new one
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    private fun insertSampleTables() {
        CoroutineScope(Dispatchers.IO).launch {
            // Check if tables already exist
            val existingTables = database.tableDao().getAllTables()
            Log.d("table list", "$existingTables")
            val dateList = arrayListOf(Date(), Date(System.currentTimeMillis() - 86400000))
            if (existingTables.isEmpty()) {
                // Insert sample tables
                val tables = listOf(
                    TableEntity(tableNumber = "Table 1", capacity = 4),
                    TableEntity(tableNumber = "Table 2", capacity = 4),
                )
                tables.forEach { table ->
                    database.tableDao().insertTable(table)
                }
            }
        }
    }
}