package com.example.trainreservation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trainreservation.R
import com.example.trainreservation.ui.model.TrainSchedule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    from: String,
    to: String,
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onSelectClick: (TrainSchedule) -> Unit
) {
    val trainList = listOf(
        TrainSchedule("Train 101", from, to, "08:00 AM", "12:30 PM", "350 TL"),
        TrainSchedule("Train 202", from, to, "10:00 AM", "02:15 PM", "420 TL"),
        TrainSchedule("Train 303", from, to, "01:30 PM", "05:40 PM", "390 TL")
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.train_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99000000))
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text("Train Schedule", color = Color.White, fontWeight = FontWeight.Bold)
                    },
                    actions = {
                        if (isLoggedIn) {
                            TextButton(onClick = onLogoutClick) {
                                Text("Logout", color = Color.White)
                            }
                        } else {
                            TextButton(onClick = onLoginClick) {
                                Text("Login", color = Color.White)
                            }
                            TextButton(onClick = onSignUpClick) {
                                Text("Sign Up", color = Color.White)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(trainList) { train ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.92f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = train.trainNumber,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D47A1)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "${train.from} → ${train.to}",
                                fontSize = 15.sp,
                                color = Color.DarkGray
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${train.departureTime} - ${train.arrivalTime}",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Price: ${train.price}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0D47A1)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = { onSelectClick(train) },
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .height(36.dp)
                            ) {
                                Text("Select")
                            }
                        }
                    }
                }
            }
        }
    }
}