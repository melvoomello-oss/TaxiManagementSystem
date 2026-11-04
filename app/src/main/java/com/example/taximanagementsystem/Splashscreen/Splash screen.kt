package com.example.taximanagementsystem.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {

    LaunchedEffect(Unit) {
        delay(3000)

        navController.navigate("login") {
            popUpTo("splash") {
                inclusive = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Taxi logo
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        Color(0xFFFFC107),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalTaxi,
                    contentDescription = "Taxi Logo",
                    modifier = Modifier.size(70.dp),
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App name
            Text(
                text = "TAXI MANAGEMENT",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "SYSTEM",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC107)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Your ride, your way",
                fontSize = 16.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(40.dp))

            CircularProgressIndicator(
                color = Color(0xFFFFC107),
                modifier = Modifier.size(35.dp),
                strokeWidth = 3.dp
            )
        }
    }
}
@Preview(ShowBackground = true)
@Composable
