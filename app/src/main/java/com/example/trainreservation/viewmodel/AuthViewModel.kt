package com.example.trainreservation.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trainreservation.data.local.User
import com.example.trainreservation.data.local.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    application: Application,
    private val userDao: UserDao
) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        restoreSession()
    }

    private fun restoreSession() {
        val savedEmail = prefs.getString("logged_in_email", null)

        if (savedEmail != null) {
            viewModelScope.launch {
                val user = userDao.findUserByEmail(savedEmail)
                _currentUser.value = user
            }
        }
    }

    private fun saveSession(email: String) {
        prefs.edit().putString("logged_in_email", email).apply()
    }

    private fun clearSession() {
        prefs.edit().remove("logged_in_email").apply()
    }

    fun checkUserExists(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val existing = userDao.findUserByEmail(email)
            onResult(existing != null)
        }
    }

    fun signUp(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val existing = userDao.findUserByEmail(email)
            if (existing != null) {
                onResult(false)
            } else {
                val newUser = User(email = email, password = password)
                userDao.insertUser(newUser)

                val savedUser = userDao.findUserByEmail(email)
                _currentUser.value = savedUser
                saveSession(email)

                onResult(true)
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = userDao.login(email, password)
            if (user != null) {
                _currentUser.value = user
                saveSession(user.email)
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        clearSession()
    }
}

class AuthViewModelFactory(
    private val application: Application,
    private val userDao: UserDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(application, userDao) as T
    }
}