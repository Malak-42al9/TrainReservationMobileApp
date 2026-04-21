package com.example.trainreservation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trainreservation.R
import com.example.trainreservation.ui.model.PassengerInfo
import com.example.trainreservation.ui.model.TrainSchedule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    selectedTrain: TrainSchedule?,
    searchDate: String,
    persons: Int,
    passengerInfo: PassengerInfo?,
    onPayClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var cardName by rememberSaveable { mutableStateOf("") }
    var cardNumber by rememberSaveable { mutableStateOf("") }
    var expiryDate by rememberSaveable { mutableStateOf("") }
    var cvv by rememberSaveable { mutableStateOf("") }

    var cardNameError by remember { mutableStateOf(false) }
    var cardNumberError by remember { mutableStateOf(false) }
    var expiryError by remember { mutableStateOf(false) }
    var cvvError by remember { mutableStateOf(false) }

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
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text("Payment", color = Color.White, fontWeight = FontWeight.Bold)
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(10.dp))


                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.92f)
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        Text(
                            "Booking Summary",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Train: ${selectedTrain?.trainNumber ?: ""}")
                        Text("Route: ${selectedTrain?.from ?: ""} → ${selectedTrain?.to ?: ""}")
                        Text("Time: ${selectedTrain?.departureTime ?: ""} - ${selectedTrain?.arrivalTime ?: ""}")
                        Text("Date: $searchDate")
                        Text("Persons: $persons")
                        Text("Passenger: ${passengerInfo?.fullName ?: ""}")

                        Text(
                            "Price: ${selectedTrain?.price ?: ""}",
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0D47A1)
                        )
                    }
                }


                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.92f)
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        Text(
                            "Card Details",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Card Name
                        OutlinedTextField(
                            value = cardName,
                            onValueChange = {
                                cardName = it
                                cardNameError = !isValidCardName(it)
                            },
                            label = { Text("Card Holder Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = cardNameError,
                            supportingText = {
                                if (cardNameError) Text("Letters only")
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))


                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = {
                                cardNumber = it.filter { ch -> ch.isDigit() }
                                cardNumberError = !isValidCardNumber(cardNumber)
                            },
                            label = { Text("Card Number") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = cardNumberError,
                            supportingText = {
                                if (cardNumberError) Text("Numbers only")
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            // Expiry
                            OutlinedTextField(
                                value = expiryDate,
                                onValueChange = {
                                    expiryDate = formatExpiry(it)
                                    expiryError = !isValidExpiry(expiryDate)
                                },
                                label = { Text("Expiry") },
                                placeholder = { Text("12/23") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                isError = expiryError,
                                supportingText = {
                                    if (expiryError) Text("MM/YY")
                                }
                            )


                            OutlinedTextField(
                                value = cvv,
                                onValueChange = {
                                    cvv = it.filter { ch -> ch.isDigit() }.take(3)
                                    cvvError = !isValidCVV(cvv)
                                },
                                label = { Text("CVV") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                isError = cvvError,
                                supportingText = {
                                    if (cvvError) Text("3 digits")
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // 🔥 Pay Button
                        Button(
                            onClick = {
                                cardNameError = cardName.isBlank() || !isValidCardName(cardName)
                                cardNumberError = cardNumber.isBlank() || !isValidCardNumber(cardNumber)
                                expiryError = expiryDate.isBlank() || !isValidExpiry(expiryDate)
                                cvvError = cvv.isBlank() || !isValidCVV(cvv)

                                if (!cardNameError && !cardNumberError && !expiryError && !cvvError) {
                                    onPayClick()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text("Pay Now", fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}

// 🔧 Helpers

fun formatExpiry(input: String): String {
    val digits = input.filter { it.isDigit() }.take(4)
    return when {
        digits.length <= 2 -> digits
        else -> digits.substring(0, 2) + "/" + digits.substring(2)
    }
}

fun isValidCardName(name: String) =
    name.isNotBlank() && name.all { it.isLetter() || it.isWhitespace() }

fun isValidCardNumber(num: String) =
    num.isNotBlank() && num.all { it.isDigit() }

fun isValidExpiry(exp: String) =
    Regex("^(0[1-9]|1[0-2])/\\d{2}$").matches(exp)

fun isValidCVV(cvv: String) =
    cvv.length == 3 && cvv.all { it.isDigit() }