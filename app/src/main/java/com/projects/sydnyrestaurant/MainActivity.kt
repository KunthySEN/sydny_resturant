package com.projects.sydnyrestaurant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.projects.sydnyrestaurant.data.AppDatabase
import com.projects.sydnyrestaurant.models.TableEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
                R.id.nav_logout -> {
                    showLogoutConfirmationDialog()
                    true
                }

                else -> super.onOptionsItemSelected(menuItem)
            }
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Do you really want to logout?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            logout(this)
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun logout(context: Context) {
        clearUserSession(context)
        navigateToWelcome(context)
        Snackbar.make(findViewById(R.id.fragment_container), "You have been logged out", Snackbar.LENGTH_SHORT).show()
    }

    private fun clearUserSession(context: Context) {
        val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    private fun navigateToWelcome(context: Context) {
        val intent = Intent(context, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
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
            if (existingTables.isEmpty()) {
                // Insert sample tables with different image resource IDs
                val tables = listOf(
                    TableEntity(tableNumber = "1", capacity = 1, imageResId = R.drawable.tbsingle),
                    TableEntity(tableNumber = "2", capacity = 4, imageResId = R.drawable.tbgroup),
                    TableEntity(tableNumber = "3", capacity = 1, imageResId = R.drawable.tbsingle),
                    TableEntity(tableNumber = "4", capacity = 2, imageResId = R.drawable.tbcouple),
                    TableEntity(tableNumber = "5", capacity = 4, imageResId = R.drawable.tbgroup),
                    TableEntity(tableNumber = "6", capacity = 2, imageResId = R.drawable.tbcouple)
                )
                tables.forEach { table ->
                    database.tableDao().insertTable(table)
                }
            }
        }
    }
}