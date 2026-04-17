package com.example.trainreservation.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Reservation::class , User::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reservationDao(): ReservationDao
    abstract fun userDao(): UserDao
}