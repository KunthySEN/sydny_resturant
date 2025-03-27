package com.projects.sydnyrestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.projects.sydnyrestaurant.data.AppDatabase
import kotlinx.coroutines.launch

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        db = AppDatabase.getDatabase(this)
        val email = intent.getStringExtra("email")

        val newPasswordInput = findViewById<TextInputEditText>(R.id.new_password)
        val confirmPasswordInput = findViewById<TextInputEditText>(R.id.confirm_password)
        val setPasswordButton = findViewById<MaterialButton>(R.id.set_password_button)

        setPasswordButton.setOnClickListener {
            val newPassword = newPasswordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()
            Log.d("value pwd :","$newPassword+"+"$confirmPassword")
            if (newPassword.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (newPassword == confirmPassword) {
                    setNewPassword(newPassword,email!!)
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setNewPassword(newPassword: String,email: String) {
        lifecycleScope.launch {
            try {
                Log.d("setNewPassword","try block")

                val user = db.userDao().getUserByEmail(email)
                Log.d("setNewPassword","$email"+"$user")

                if (user != null) {
                    user.password = newPassword
                    db.userDao().updateUser(user)
                    Toast.makeText(this@ResetPasswordActivity, "Password has been reset", Toast.LENGTH_SHORT).show()
                    // Navigate to LoginActivity
                    val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@ResetPasswordActivity, "User not found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ResetPasswordActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}