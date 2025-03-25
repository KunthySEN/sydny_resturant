package com.projects.sydnyrestaurant

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton


class ThankYouActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_thank_you)

        val homeButton: MaterialButton = findViewById(R.id.home)
        homeButton.setOnClickListener {
            // Navigate back to Home screen
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}
