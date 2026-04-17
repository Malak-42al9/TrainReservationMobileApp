package com.example.trainreservation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.room.Room
import com.example.trainreservation.data.local.AppDatabase
import com.example.trainreservation.navigation.AppNavigation
import com.example.trainreservation.ui.theme.TrainReservationTheme
import com.example.trainreservation.viewmodel.AuthViewModel
import com.example.trainreservation.viewmodel.AuthViewModelFactory
import com.example.trainreservation.viewmodel.ReservationViewModel
import com.example.trainreservation.viewmodel.ReservationViewModelFactory

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "train_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    private val reservationViewModel: ReservationViewModel by viewModels {
        ReservationViewModelFactory(db)
    }

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(application, db.userDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TrainReservationTheme {
                AppNavigation(
                    reservationViewModel = reservationViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}