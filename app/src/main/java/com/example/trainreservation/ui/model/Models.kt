package com.example.trainreservation.ui.model

data class TrainSchedule(
    val trainNumber: String,
    val from: String,
    val to: String,
    val departureTime: String,
    val arrivalTime: String,
    val price: String
)

data class PassengerInfo(
    val fullName: String,
    val email: String,
    val phone: String,
    val idNumber: String,
    val gender: String
)

data class Reservation(
    val id: Int,
    val trainNumber: String,
    val from: String,
    val to: String,
    val departureTime: String,
    val arrivalTime: String,
    val price: String,
    val passengerName: String,
    val date: String,
    val persons: Int
)