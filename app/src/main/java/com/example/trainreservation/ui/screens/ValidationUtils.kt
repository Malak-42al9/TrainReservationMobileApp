package com.example.trainreservation.ui.screens

fun isValidEmailFormat(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}