package com.example.trainreservation.ui.model

import com.example.trainreservation.data.local.Reservation as DbReservation

fun DbReservation.toUiReservation(): Reservation {
    return Reservation(
        id = id,
        trainNumber = trainNumber,
        from = fromStation,
        to = toStation,
        departureTime = departureTime,
        arrivalTime = arrivalTime,
        price = price,
        passengerName = passengerName,
        date = date,
        persons = persons
    )
}