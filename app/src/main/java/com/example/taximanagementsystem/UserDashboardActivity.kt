package com.example.taximanagementsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UserDashboardActivity : ComponentActivity() {

    private lateinit var pickupLocation: EditText
    private lateinit var destinationLocation: EditText
    private lateinit var bookTaxi: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_dashboard)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pickupLocation = findViewById(R.id.pickupLocation)
        destinationLocation = findViewById(R.id.destination)
        bookTaxi = findViewById(R.id.bookTaxi)

        bookTaxi.setOnClickListener {
            val pickup = pickupLocation.text.toString().trim()
            val destination = destinationLocation.text.toString().trim()

            if (pickup.isEmpty() || destination.isEmpty()) {
                Toast.makeText(this, "Please enter both pickup and destination locations", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, BookingActivity::class.java).apply {
                    putExtra("PICKUP_LOCATION", pickup)
                    putExtra("DESTINATION_LOCATION", destination)
                    putExtra("DISTANCE", "5.0")
                }
                startActivity(intent)
            }
        }
    }
}
