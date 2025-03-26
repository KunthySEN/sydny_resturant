package com.projects.sydnyrestaurant

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.projects.sydnyrestaurant.data.AppDatabase
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        db = AppDatabase.getDatabase(this)

        val emailEditText: EditText = findViewById(R.id.email)
        val resetPasswordButton: Button = findViewById(R.id.reset_password_button)
        val newPasswordEditText: EditText = findViewById(R.id.new_password)
        val confirmPasswordEditText: EditText = findViewById(R.id.confirm_password)
        val setPasswordButton: Button = findViewById(R.id.set_password_button)

        resetPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            resetPassword(email)
        }

        setPasswordButton.setOnClickListener {
            val newPassword = newPasswordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please enter and confirm your new password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            setNewPassword(newPassword)
        }
    }

    private fun resetPassword(email: String) {
        lifecycleScope.launch {
            val user = db.userDao().getUserByEmail(email)
            if (user != null) {
                // Hide reset password button and show new password fields
                findViewById<Button>(R.id.reset_password_button).visibility = View.GONE
                findViewById<EditText>(R.id.new_password).visibility = View.VISIBLE
                findViewById<EditText>(R.id.confirm_password).visibility = View.VISIBLE
                findViewById<Button>(R.id.set_password_button).visibility = View.VISIBLE
                Toast.makeText(this@ForgotPasswordActivity, "Please enter your new password", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@ForgotPasswordActivity, "No account found with this email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setNewPassword(newPassword: String) {
        lifecycleScope.launch {
            try {
                val email = findViewById<EditText>(R.id.email).text.toString()
                val user = db.userDao().getUserByEmail(email)
                if (user != null) {
                    user.password = newPassword
                    db.userDao().updateUser(user)
                    Toast.makeText(this@ForgotPasswordActivity, "Password updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@ForgotPasswordActivity, "User not found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ForgotPasswordActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}