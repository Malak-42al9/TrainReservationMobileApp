package com.example.trainreservation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import com.example.trainreservation.ui.model.Reservation
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trainreservation.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.collections.take

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    reservations: List<Reservation>,
    isLoggedIn: Boolean,
    onOpenReservationsClick: () -> Unit,
    onSearchClick: (String, String, String) -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var from by remember { mutableStateOf("") }
    var to by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    var fromError by remember { mutableStateOf(false) }
    var toError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }

    val todayUtcMillis = remember {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.timeInMillis
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayUtcMillis
            }
        }
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
                        Text(
                            text = "Search Trains",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
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
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ReservationPreviewSection(
                        reservations = reservations,
                        onOpenReservationsClick = onOpenReservationsClick
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.92f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "Find Your Trip",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D47A1)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = from,
                                onValueChange = {
                                    from = it
                                    fromError = !isValidLocation(it)
                                },
                                label = { Text("From") },
                                isError = fromError,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                singleLine = true,
                                supportingText = {
                                    if (fromError) {
                                        Text("Enter a valid city name")
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = to,
                                onValueChange = {
                                    to = it
                                    toError = !isValidLocation(it)
                                },
                                label = { Text("To") },
                                isError = toError,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                singleLine = true,
                                supportingText = {
                                    if (toError) {
                                        Text("Enter a valid city name")
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = dateText,
                                    onValueChange = {},
                                    label = { Text("Date") },
                                    placeholder = { Text("Select date") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    singleLine = true,
                                    readOnly = true,
                                    isError = dateError
                                )

                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clickable { showDatePicker = true }
                                )
                            }

                            if (dateError) {
                                Text(
                                    text = "Please select a date",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    fromError = from.isBlank() || !isValidLocation(from)
                                    toError = to.isBlank() || !isValidLocation(to)
                                    dateError = dateText.isBlank()

                                    if (!fromError && !toError && !dateError) {
                                        onSearchClick(from, to, dateText)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Search", fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { selectedMillis ->
                                dateText = convertMillisToDate(selectedMillis)
                                dateError = false
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
fun ReservationPreviewSection(
    reservations: List<Reservation>,
    onOpenReservationsClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.92f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Reservations",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1)
                )

                Text(
                    text = "See all",
                    color = Color(0xFF0D47A1),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onOpenReservationsClick() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (reservations.isEmpty()) {
                Text(
                    text = "No reservations yet.",
                    color = Color.Gray
                )
            } else {
                val preview = reservations.take(2)
                preview.forEach { reservation ->
                    Text(
                        text = "${reservation.trainNumber} • ${reservation.from} → ${reservation.to}",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "${reservation.date} • ${reservation.departureTime} - ${reservation.arrivalTime}",
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun isValidLocation(text: String): Boolean {
    return text.isNotBlank() && text.all { it.isLetter() || it.isWhitespace() }
}