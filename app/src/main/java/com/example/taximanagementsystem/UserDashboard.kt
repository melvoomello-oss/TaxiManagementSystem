package com.example.taximanagementsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TaxiBlue = Color(0xFF1565C0)
private val TaxiBackground = Color(0xFFF5F7FA)

data class TaxiBookingItem(
    val destination: String,
    val date: String,
    val fare: String,
    val status: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboard(
    userName: String = "Melvin",
    onBookTaxi: (String, String) -> Unit = { _, _ -> },
    onNavigate: (String) -> Unit = {}
) {
    var pickup by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }

    val bookings = listOf(
        TaxiBookingItem("Westlands", "Today, 10:30 AM", "KSh 450", "Completed"),
        TaxiBookingItem("Kilimani", "Yesterday, 4:15 PM", "KSh 350", "Completed"),
        TaxiBookingItem("Nairobi CBD", "Yesterday, 8:00 AM", "KSh 250", "Cancelled")
    )

    Scaffold(
        containerColor = TaxiBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Taxi Management", fontWeight = FontWeight.Bold)
                        Text("User Dashboard", fontSize = 12.sp)
                    }
                },
                navigationIcon = {
                    Icon(
                        Icons.Default.LocalTaxi,
                        contentDescription = "Taxi",
                        tint = TaxiBlue,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                },
                actions = {
                    IconButton(onClick = { onNavigate("notifications") }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { onNavigate("profile") }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val tabs = listOf("Home", "Bookings", "Profile")
                val icons = listOf(
                    Icons.Default.Home,
                    Icons.Default.History,
                    Icons.Default.Person
                )

                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            onNavigate(tab.lowercase())
                        },
                        icon = { Icon(icons[index], contentDescription = tab) },
                        label = { Text(tab) }
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Hello, $userName!", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                Text(text = "Where would you like to go today?", color = Color.Gray)
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Book a Taxi", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                        OutlinedTextField(
                            value = pickup,
                            onValueChange = { pickup = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Pickup Location") },
                            leadingIcon = { Icon(Icons.Default.MyLocation, contentDescription = null) },
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = destination,
                            onValueChange = { destination = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Destination") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                            singleLine = true
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Estimated fare", color = Color.Gray)
                            Text("KSh 450", fontWeight = FontWeight.Bold, color = TaxiBlue)
                        }

                        Button(
                            onClick = {
                                if (pickup.isNotBlank() && destination.isNotBlank()) {
                                    onBookTaxi(pickup, destination)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = TaxiBlue)
                        ) {
                            Icon(Icons.Default.LocalTaxi, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Book Taxi Now")
                        }
                    }
                }
            }

            item {
                Text("Quick Services", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ServiceCard(
                        title = "Book Now",
                        icon = Icons.Default.LocalTaxi,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedTab = 0 }
                    )
                    ServiceCard(
                        title = "My Trips",
                        icon = Icons.Default.History,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedTab = 1
                            onNavigate("bookings")
                        }
                    )
                    ServiceCard(
                        title = "Support",
                        icon = Icons.Default.SupportAgent,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("support") }
                    )
                }
            }

            item {
                Text("Recent Bookings", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            items(bookings) { booking ->
                BookingCard(booking)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ServiceCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = TaxiBlue)
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun BookingCard(booking: TaxiBookingItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocalTaxi,
                contentDescription = null,
                tint = TaxiBlue,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(booking.destination, fontWeight = FontWeight.Bold)
                Text(booking.date, fontSize = 12.sp, color = Color.Gray)
                Text(
                    booking.status,
                    fontSize = 12.sp,
                    color = if (booking.status == "Completed") Color(0xFF2E7D32) else Color.Red
                )
            }
            Text(booking.fare, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserDashboardPreview() {
    MaterialTheme {
        UserDashboard()
    }
}
