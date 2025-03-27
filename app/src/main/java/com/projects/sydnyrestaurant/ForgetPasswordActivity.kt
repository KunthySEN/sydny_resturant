package com.projects.sydnyrestaurant

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.projects.sydnyrestaurant.data.AppDatabase
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var actionBack: ImageView
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide() // Hide the default action bar
        setContentView(R.layout.activity_forget_password)
        setupToolbar()

        db = AppDatabase.getDatabase(this)

        val emailInput = findViewById<TextInputEditText>(R.id.email)
        val resetPasswordButton = findViewById<MaterialButton>(R.id.reset_password_button)

        resetPasswordButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            if (email.isNotEmpty()) {
                resetPassword(email)
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupToolbar() {
        title = findViewById(R.id.appBarTitle)
        actionBack = findViewById(R.id.action_back)
        actionBack.setOnClickListener {
            onBackPressed()
        }
        title.text = "Forgot Password"
    }

    private fun resetPassword(email: String) {
        lifecycleScope.launch {
            val user = db.userDao().getUserByEmail(email)
            if (user != null) {
                Toast.makeText(this@ForgotPasswordActivity, "Password reset link sent to $email", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java)
                intent.putExtra("email", email)
                startActivity(intent)
            } else {
                Toast.makeText(this@ForgotPasswordActivity, "No account found with this email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}