package com.example.trainreservation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import com.example.trainreservation.ui.model.PassengerInfo

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun PassengerScreen(
    onContinueClick: (PassengerInfo) -> Unit
) {
    var fullName by remember { mutableStateOf("Malak Adel Mohammed") }
    var email by remember { mutableStateOf("malak@gmail.com") }
    var phone by remember { mutableStateOf("0534875875") }
    var idNumber by remember { mutableStateOf("76539554") }
    var gender by remember { mutableStateOf("female") }
    var genderExpanded by remember { mutableStateOf(false) }


    var fullNameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var idError by remember { mutableStateOf(false) }
    var genderError by remember { mutableStateOf(false) }

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
                        Text("Passenger Details", color = Color.White, fontWeight = FontWeight.Bold)
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
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
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
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Enter Passenger Information",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = fullName,
                            onValueChange = {
                                fullName = it
                                fullNameError = !isValidName(it)
                            },
                            label = { Text("Full Name") },
                            isError = fullNameError,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            supportingText = {
                                if (fullNameError) {
                                    Text("Full name must contain letters only")
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                emailError = !isValidEmail(it)
                            },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = emailError,
                            supportingText = {
                                if (emailError) {
                                    Text("Email must be a real email")
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = phone,
                            onValueChange = {
                                phone = it
                                phoneError = !isValidPhone(it)
                            },
                            label = { Text("Phone Number") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = phoneError,
                            singleLine = true,
                            supportingText = {
                                if (phoneError) {
                                    Text("Phone number must contain numbers only")
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = idNumber,
                            onValueChange = {
                                idNumber = it
                                idError = !isValidId(it)
                            },
                            label = { Text("ID / Passport Number") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = idError,
                            singleLine = true, supportingText = {
                                if (idError) {
                                    Text("Passport/ID must contain of numbers only")
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        ExposedDropdownMenuBox(
                            expanded = genderExpanded,
                            onExpandedChange = { genderExpanded = !genderExpanded }
                        ) {
                            OutlinedTextField(
                                value = gender,
                                onValueChange = {},
                                label = { Text("Gender") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                readOnly = true,
                                singleLine = true
                            )

                            ExposedDropdownMenu(
                                expanded = genderExpanded,
                                onDismissRequest = { genderExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Male") },
                                    onClick = {
                                        gender = "Male"
                                        genderExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Female") },
                                    onClick = {
                                        gender = "Female"
                                        genderExpanded = false
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                fullNameError = fullName.isBlank() || !isValidName(fullName)
                                emailError = email.isBlank() || !isValidEmail(email)
                                phoneError = phone.isBlank() || !isValidPhone(phone)
                                idError = idNumber.isBlank() || !isValidId(idNumber)
                                genderError = gender.isBlank()

                                if (!fullNameError && !emailError && !phoneError && !idError && !genderError) {
                                    onContinueClick(
                                        PassengerInfo(
                                            fullName = fullName,
                                            email = email,
                                            phone = phone,
                                            idNumber = idNumber,
                                            gender = gender
                                        )
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text("Continue to Payment", fontSize = 17.sp)
                        }
                    }
                }
            }
        }
    }
}

fun isValidName(name: String): Boolean {
    return name.all { it.isLetter() || it.isWhitespace() } && name.isNotBlank()
}

fun isValidPhone(phone: String): Boolean {
    return phone.all { it.isDigit() }
}

fun isValidId(id: String): Boolean {
    return id.all { it.isDigit() } && id.isNotBlank()
}

fun isValidEmail(email: String): Boolean {
    return email.contains("@") && email.contains(".")
}