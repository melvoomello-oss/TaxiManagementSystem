package com.example.taximanagementsystem

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore

class BookingActivity : ComponentActivity() {

    private lateinit var pickupText: TextView
    private lateinit var dropText: TextView
    private lateinit var taxiText: TextView
    private lateinit var taxiSubtext: TextView
    private lateinit var fareText: TextView
    private lateinit var confirmationButton: Button
    private lateinit var statusCard: MaterialCardView
    private lateinit var statusDetail: TextView

    private lateinit var driverCard: MaterialCardView
    private lateinit var driverNameText: TextView
    private lateinit var driverVehicleText: TextView
    private lateinit var driverPhoneText: TextView
    private lateinit var btnCallDriver: Button

    private lateinit var mapTrackingCard: MaterialCardView
    private lateinit var etaText: TextView
    private lateinit var mapRouteText: TextView

    private lateinit var miniTaxiCard: MaterialCardView
    private lateinit var suvRideCard: MaterialCardView
    private lateinit var miniTaxiFareText: TextView
    private lateinit var suvFareText: TextView

    private var selectedVehicle = "Mini Taxi"
    private var selectedSeats = 4
    private var distance = 0.0
    private var calculatedFare = 0
    private val driverPhoneNumber = "+254712345678"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_booking)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        pickupText = findViewById(R.id.pickupText)
        dropText = findViewById(R.id.dropText)
        taxiText = findViewById(R.id.taxiText)
        taxiSubtext = findViewById(R.id.taxiSubtext)
        fareText = findViewById(R.id.fareText)
        confirmationButton = findViewById(R.id.confirmationButton)
        statusCard = findViewById(R.id.statusCard)
        statusDetail = findViewById(R.id.statusDetail)

        driverCard = findViewById(R.id.driverCard)
        driverNameText = findViewById(R.id.driverNameText)
        driverVehicleText = findViewById(R.id.driverVehicleText)
        driverPhoneText = findViewById(R.id.driverPhoneText)
        btnCallDriver = findViewById(R.id.btnCallDriver)

        mapTrackingCard = findViewById(R.id.mapTrackingCard)
        etaText = findViewById(R.id.etaText)
        mapRouteText = findViewById(R.id.mapRouteText)

        miniTaxiCard = findViewById(R.id.miniTaxiCard)
        suvRideCard = findViewById(R.id.suvRideCard)
        miniTaxiFareText = findViewById(R.id.miniTaxiFareText)
        suvFareText = findViewById(R.id.suvFareText)

        // Retrieve intent extras passed from MainActivity
        val pickup = intent.getStringExtra("PICKUP_LOCATION") ?: "Not specified"
        val drop = intent.getStringExtra("DESTINATION_LOCATION") ?: "Not specified"
        val distanceStr = intent.getStringExtra("DISTANCE") ?: "0"

        distance = distanceStr.toDoubleOrNull() ?: 5.0
        if (distance <= 0) distance = 5.0

        pickupText.text = "Pickup: $pickup"
        dropText.text = "Drop: $drop ($distance km)"

        // Compute option fares based on distance
        val miniFare = (150 + (distance * 80)).toInt()
        val suvFare = (300 + (distance * 140)).toInt()

        miniTaxiFareText.text = "Ksh $miniFare"
        suvFareText.text = "Ksh $suvFare"

        // Default selection: Mini Taxi (4-Seater)
        selectMiniTaxi(miniFare)

        // Card Selection listeners
        miniTaxiCard.setOnClickListener {
            selectMiniTaxi(miniFare)
        }

        suvRideCard.setOnClickListener {
            selectSUV(suvFare)
        }

        // Call Driver Action
        btnCallDriver.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$driverPhoneNumber")
            }
            startActivity(callIntent)
        }

        // Handle Confirmation Button Click
        confirmationButton.setOnClickListener {
            val driverEtaMinutes = (4..8).random()
            val totalTripMinutes = (driverEtaMinutes + (distance * 2.5)).toInt()

            // Display confirmation card, driver contact card, and live map tracking card
            statusCard.visibility = View.VISIBLE
            statusDetail.text = "Your $selectedVehicle ($selectedSeats-Seater) ride from $pickup to $drop ($distance km) is confirmed! Driver is on the way."

            driverCard.visibility = View.VISIBLE
            driverNameText.text = "David Mwangi (★ 4.9 Rating)"
            driverVehicleText.text = "Booked Ride: $selectedVehicle ($selectedSeats-Seater) • License: KDA 892X"
            driverPhoneText.text = "Driver Phone: $driverPhoneNumber"

            mapTrackingCard.visibility = View.VISIBLE
            etaText.text = "⏱ Driver Arrival ETA: $driverEtaMinutes mins • Trip Duration: $totalTripMinutes mins"
            mapRouteText.text = "🚏 $pickup ➔ 🚖 $selectedVehicle En Route ($driverEtaMinutes mins) ➔ 🏁 $drop ($distance km)"

            confirmationButton.text = "Booking Confirmed"
            confirmationButton.isEnabled = false

            Toast.makeText(applicationContext, "$selectedVehicle Ride Confirmed! Driver David is arriving in $driverEtaMinutes mins.", Toast.LENGTH_LONG).show()

            // Asynchronously push to Firebase without blocking UI
            try {
                val bookingData = hashMapOf(
                    "pickupLocation" to pickup,
                    "destinationLocation" to drop,
                    "vehicleType" to selectedVehicle,
                    "seats" to selectedSeats,
                    "distanceKm" to distance,
                    "totalFare" to calculatedFare,
                    "driverName" to "David Mwangi",
                    "driverPhone" to driverPhoneNumber,
                    "driverPlate" to "KDA 892X",
                    "etaMinutes" to driverEtaMinutes,
                    "timestamp" to System.currentTimeMillis()
                )

                FirebaseFirestore.getInstance()
                    .collection("bookings")
                    .add(bookingData)
            } catch (e: Exception) {
                // Firebase optional sync fallback
            }
        }
    }

    private fun selectMiniTaxi(miniFare: Int) {
        selectedVehicle = "Mini Taxi"
        selectedSeats = 4
        calculatedFare = miniFare

        miniTaxiCard.strokeColor = Color.parseColor("#166534")
        miniTaxiCard.strokeWidth = 6
        miniTaxiCard.cardElevation = 8f

        suvRideCard.strokeColor = Color.parseColor("#E5E7EB")
        suvRideCard.strokeWidth = 2
        suvRideCard.cardElevation = 2f

        taxiText.text = "Selected Taxi: Mini Taxi (4-Seater)"
        taxiSubtext.text = "Economical 4-seater city ride ($distance km)"
        fareText.text = "Estimated fare: Ksh $miniFare"
    }

    private fun selectSUV(suvFare: Int) {
        selectedVehicle = "SUV"
        selectedSeats = 6
        calculatedFare = suvFare

        suvRideCard.strokeColor = Color.parseColor("#166534")
        suvRideCard.strokeWidth = 6
        suvRideCard.cardElevation = 8f

        miniTaxiCard.strokeColor = Color.parseColor("#E5E7EB")
        miniTaxiCard.strokeWidth = 2
        miniTaxiCard.cardElevation = 2f

        taxiText.text = "Selected Taxi: SUV (6-Seater)"
        taxiSubtext.text = "Spacious 6-seater vehicle for groups & luggage ($distance km)"
        fareText.text = "Estimated fare: Ksh $suvFare"
    }
}
