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

class LoginActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        db = AppDatabase.getDatabase(this)

        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.sign_in_button)
        val createAnAccount: TextView = findViewById(R.id.create_an_account)
        val forgotPasswordTextView: TextView = findViewById(R.id.forget_pwd)

        forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            loginUser (email, password)
        }

        createAnAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser (email: String, password: String) {
        lifecycleScope.launch {
            val user: UserEntity? = db.userDao().getUser(email, password)
            if (user != null) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this@LoginActivity, "Login Failed: Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}