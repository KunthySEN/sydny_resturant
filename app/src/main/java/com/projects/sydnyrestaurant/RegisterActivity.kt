package com.projects.sydnyrestaurant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.projects.sydnyrestaurant.data.AppDatabase
import com.projects.sydnyrestaurant.models.UserEntity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        db = AppDatabase.getDatabase(this)

        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val confirmPasswordEditText: EditText = findViewById(R.id.confirm_password)
        val signUpButton: Button = findViewById(R.id.sign_up_button)
        val alreadyHaveAccount: TextView = findViewById(R.id.have_an_account)

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (password == confirmPassword) {
                registerUser (email, password)
            } else {
                Toast.makeText(this, "Registration Failed: Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

        alreadyHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser (email: String, password: String) {
        lifecycleScope.launch {
            val existingUser  = db.userDao().getUser (email, password)
            if (existingUser  == null) {
                db.userDao().insert(UserEntity(email, password))
                Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this@RegisterActivity, "Registration Failed: User already exists", Toast.LENGTH_SHORT).show()
            }
        }
    }
}