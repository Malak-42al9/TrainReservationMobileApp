package com.example.trainreservation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trainreservation.data.local.AppDatabase
import com.example.trainreservation.data.local.Reservation as DbReservation
import com.example.trainreservation.ui.model.Reservation
import com.example.trainreservation.ui.model.toUiReservation
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReservationViewModel(
    private val db: AppDatabase
) : ViewModel() {

    val reservations: StateFlow<List<Reservation>> =
        db.reservationDao()
            .getAllReservations()
            .map { list -> list.map { it.toUiReservation() } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun saveReservation(
        passengerName: String,
        from: String,
        to: String,
        trainNumber: String,
        departureTime: String,
        arrivalTime: String,
        price: String,
        date: String,
        persons: Int
    ) {
        viewModelScope.launch {
            db.reservationDao().insertReservation(
                DbReservation(
                    passengerName = passengerName,
                    fromStation = from,
                    toStation = to,
                    trainNumber = trainNumber,
                    departureTime = departureTime,
                    arrivalTime = arrivalTime,
                    price = price,
                    date = date,
                    persons = persons
                )
            )
        }
    }
    fun deleteReservation(reservation: Reservation) {
        viewModelScope.launch {
            db.reservationDao().deleteReservationById(reservation.id)
        }
    }
}

class ReservationViewModelFactory(
    private val db: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReservationViewModel(db) as T
    }
}