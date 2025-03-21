package com.projects.sydnyrestaurant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)

        //Check if the user is already registered
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isRegistered = sharedPreferences.getBoolean("isRegistered", false)

        if (isRegistered) {
            // If the user is already registered, go to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close WelcomeActivity
            return
        }

        //Initialize the Register button and set an onClickListener
        val registerButton: Button = findViewById(R.id.registerButton)
        var loginButton : Button = findViewById(R.id.btnLogin)
        registerButton.setOnClickListener {
            // Intent to navigate to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}