package com.example.taximanagementsystem

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : ComponentActivity() {

    private lateinit var pickupInput: EditText
    private lateinit var destinationInput: EditText
    private lateinit var distanceInput: EditText
    private lateinit var fareText: TextView
    private lateinit var btnBookRide: Button
    private var isAutoCalculating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        pickupInput = findViewById(R.id.pickupInput)
        destinationInput = findViewById(R.id.destinationInput)
        distanceInput = findViewById(R.id.distanceInput)
        fareText = findViewById(R.id.fareText)
        btnBookRide = findViewById(R.id.btnBookRide)

        // Automatically calculate distance when pickup or destination input changes
        val locationWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                autoCalculateDistanceAndFare()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        pickupInput.addTextChangedListener(locationWatcher)
        destinationInput.addTextChangedListener(locationWatcher)

        // Calculate fare when distance input changes manually
        distanceInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isAutoCalculating) {
                    calculateFare()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Handle Book Ride button click
        btnBookRide.setOnClickListener {
            val pickup = pickupInput.text.toString().trim()
            val destination = destinationInput.text.toString().trim()

            if (pickup.isEmpty() || destination.isEmpty()) {
                Toast.makeText(this, "Please enter pickup and destination locations", Toast.LENGTH_SHORT).show()
            } else {
                var distanceStr = distanceInput.text.toString().trim()
                if (distanceStr.isEmpty() || (distanceStr.toDoubleOrNull() ?: 0.0) <= 0) {
                    val dist = estimateDistanceBetween(pickup, destination)
                    distanceStr = dist.toString()
                }

                val intent = Intent(this, BookingActivity::class.java).apply {
                    putExtra("PICKUP_LOCATION", pickup)
                    putExtra("DESTINATION_LOCATION", destination)
                    putExtra("DISTANCE", distanceStr)
                }
                startActivity(intent)
            }
        }
    }

    private fun autoCalculateDistanceAndFare() {
        val pickup = pickupInput.text.toString().trim()
        val destination = destinationInput.text.toString().trim()

        if (pickup.isNotEmpty() && destination.isNotEmpty()) {
            val distance = estimateDistanceBetween(pickup, destination)
            isAutoCalculating = true
            distanceInput.setText(distance.toString())
            isAutoCalculating = false

            val ratePerKm = 100 // Ksh 100 per km
            val baseFare = 200  // Ksh 200 base fee
            val totalFare = baseFare + (distance * ratePerKm)
            fareText.text = "Calculated distance: $distance km\nEstimated fare: Ksh ${totalFare.toInt()}"
        }
    }

    private fun calculateFare() {
        val distanceStr = distanceInput.text.toString().trim()
        val distance = distanceStr.toDoubleOrNull()
        if (distance != null && distance > 0) {
            val ratePerKm = 100 // Ksh 100 per km
            val baseFare = 200  // Ksh 200 base fee
            val totalFare = baseFare + (distance * ratePerKm)
            fareText.text = "Calculated distance: $distance km\nEstimated fare: Ksh ${totalFare.toInt()}"
        } else {
            fareText.text = "Estimated fare: Ksh 0"
        }
    }

    private fun estimateDistanceBetween(pickup: String, destination: String): Double {
        if (pickup.isEmpty() || destination.isEmpty()) return 0.0
        val pLower = pickup.lowercase()
        val dLower = destination.lowercase()
        if (pLower == dLower) return 1.0

        val combinedHash = Math.abs((pLower + dLower).hashCode())
        val baseKm = (combinedHash % 25) + 3 // 3 to 28 km range for typical rides
        val charDiff = Math.abs(pickup.length - destination.length) * 0.5
        val estimatedKm = baseKm + charDiff
        return Math.round(estimatedKm * 10.0) / 10.0
    }
}
