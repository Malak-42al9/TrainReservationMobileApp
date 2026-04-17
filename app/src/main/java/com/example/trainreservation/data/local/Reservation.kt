package com.example.trainreservation.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val passengerName: String,
    val fromStation: String,
    val toStation: String,
    val trainNumber: String,
    val departureTime: String,
    val arrivalTime: String,
    val price: String,
    val date: String,
    val persons: Int
)