package com.projects.sydnyrestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.projects.sydnyrestaurant.data.AppDatabase
import kotlinx.coroutines.launch

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var actionBack: ImageView
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Hide the default action bar

        setContentView(R.layout.activity_reset_password)
        setupToolbar()

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

    private fun setupToolbar() {
        title = findViewById(R.id.appBarTitle)
        actionBack = findViewById(R.id.action_back)
        actionBack.setOnClickListener {
            onBackPressed()
        }
        title.text = "Reset Password"    }


    private fun setNewPassword(newPassword: String,email: String) {
        lifecycleScope.launch {
            try {
                val user = db.userDao().getUserByEmail(email)
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