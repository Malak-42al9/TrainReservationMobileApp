package com.example.trainreservation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trainreservation.ui.model.PassengerInfo
import com.example.trainreservation.ui.model.TrainSchedule
import com.example.trainreservation.ui.screens.LoginScreen
import com.example.trainreservation.ui.screens.PassengerScreen
import com.example.trainreservation.ui.screens.PaymentScreen
import com.example.trainreservation.ui.screens.ReservationsScreen
import com.example.trainreservation.ui.screens.ScheduleScreen
import com.example.trainreservation.ui.screens.SearchScreen
import com.example.trainreservation.ui.screens.SignUpScreen
import com.example.trainreservation.ui.screens.StartScreen
import com.example.trainreservation.viewmodel.AuthViewModel
import com.example.trainreservation.viewmodel.ReservationViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    reservationViewModel: ReservationViewModel,
    authViewModel: AuthViewModel
) {
    var showPaymentSuccessMessage by rememberSaveable { mutableStateOf(false) }
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val reservations = reservationViewModel.reservations.collectAsState().value
    val currentUser = authViewModel.currentUser.collectAsState().value
    val isLoggedIn = currentUser != null

    var authReturnRoute by rememberSaveable { mutableStateOf(AppRoutes.SEARCH) }
    var pendingTrain by remember { mutableStateOf<TrainSchedule?>(null) }

    var searchFrom by rememberSaveable { mutableStateOf("") }
    var searchTo by rememberSaveable { mutableStateOf("") }
    var searchDate by rememberSaveable { mutableStateOf("") }

    var selectedTrain by remember { mutableStateOf<TrainSchedule?>(null) }
    var passengerInfo by remember { mutableStateOf<PassengerInfo?>(null) }

    fun navigateAfterAuth() {
        if (authReturnRoute == AppRoutes.PASSENGER && pendingTrain != null) {
            selectedTrain = pendingTrain
            pendingTrain = null
        }

        navController.navigate(authReturnRoute) {
            popUpTo(AppRoutes.LOGIN) { inclusive = true }
            popUpTo(AppRoutes.SIGNUP) { inclusive = true }
            launchSingleTop = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,

            // CHANGE THIS:
            // If you don't want the old Start screen anymore, use SEARCH
            startDestination = AppRoutes.START
        ) {

            composable(AppRoutes.START) {
                StartScreen(
                    onStartClick = {
                        navController.navigate(AppRoutes.SEARCH)
                    }
                )
            }

            composable(AppRoutes.SEARCH) {
                LaunchedEffect(showPaymentSuccessMessage) {
                    if (showPaymentSuccessMessage) {
                        snackbarHostState.showSnackbar("Payment Successful ✅")
                        showPaymentSuccessMessage = false
                    }
                }

                SearchScreen(
                    reservations = reservations,
                    isLoggedIn = isLoggedIn,
                    onOpenReservationsClick = {
                        if (isLoggedIn) {
                            navController.navigate(AppRoutes.RESERVATIONS)
                        } else {
                            authReturnRoute = AppRoutes.RESERVATIONS
                            navController.navigate(AppRoutes.LOGIN)
                        }
                    },
                    onSearchClick = { from, to, date ->
                        searchFrom = from
                        searchTo = to
                        searchDate = date
                        navController.navigate(AppRoutes.SCHEDULE)
                    },
                    onLoginClick = {
                        authReturnRoute = AppRoutes.SEARCH
                        navController.navigate(AppRoutes.LOGIN)
                    },
                    onSignUpClick = {
                        authReturnRoute = AppRoutes.SEARCH
                        navController.navigate(AppRoutes.SIGNUP)
                    },
                    onLogoutClick = {
                        authViewModel.logout()
                        scope.launch {
                            snackbarHostState.showSnackbar("Logged out successfully")
                        }
                    }
                )
            }

            composable(AppRoutes.SCHEDULE) {
                ScheduleScreen(
                    from = searchFrom,
                    to = searchTo,
                    isLoggedIn = isLoggedIn,
                    onLoginClick = {
                        authReturnRoute = AppRoutes.SCHEDULE
                        navController.navigate(AppRoutes.LOGIN)
                    },
                    onSignUpClick = {
                        authReturnRoute = AppRoutes.SCHEDULE
                        navController.navigate(AppRoutes.SIGNUP)
                    },
                    onLogoutClick = {
                        authViewModel.logout()
                        scope.launch {
                            snackbarHostState.showSnackbar("Logged out successfully")
                        }
                    },
                    onSelectClick = { train ->
                        if (isLoggedIn) {
                            selectedTrain = train
                            navController.navigate(AppRoutes.PASSENGER)
                        } else {
                            pendingTrain = train
                            authReturnRoute = AppRoutes.PASSENGER
                            navController.navigate(AppRoutes.LOGIN)
                        }
                    }
                )
            }

            composable(AppRoutes.PASSENGER) {
                LaunchedEffect(isLoggedIn) {
                    if (!isLoggedIn) {
                        authReturnRoute = AppRoutes.PASSENGER
                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(AppRoutes.PASSENGER) { inclusive = true }
                        }
                    }
                }

                if (isLoggedIn) {
                    PassengerScreen(
                        onContinueClick = { info ->
                            passengerInfo = info
                            navController.navigate(AppRoutes.PAYMENT)
                        }
                    )
                }
            }

            composable(AppRoutes.PAYMENT) {
                LaunchedEffect(isLoggedIn) {
                    if (!isLoggedIn) {
                        authReturnRoute = AppRoutes.PAYMENT
                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(AppRoutes.PAYMENT) { inclusive = true }
                        }
                    }
                }

                if (isLoggedIn) {
                    PaymentScreen(
                        selectedTrain = selectedTrain,
                        searchDate = searchDate,
                        persons = 1,
                        passengerInfo = passengerInfo,
                        onPayClick = {
                            val train = selectedTrain
                            val passenger = passengerInfo

                            if (train != null && passenger != null) {
                                reservationViewModel.saveReservation(
                                    passengerName = passenger.fullName,
                                    from = train.from,
                                    to = train.to,
                                    trainNumber = train.trainNumber,
                                    departureTime = train.departureTime,
                                    arrivalTime = train.arrivalTime,
                                    price = train.price,
                                    date = searchDate,
                                    persons = 1
                                )
                            }

                            showPaymentSuccessMessage = true

                            navController.navigate(AppRoutes.SEARCH) {
                                popUpTo(AppRoutes.SEARCH) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }

            composable(AppRoutes.RESERVATIONS) {
                LaunchedEffect(isLoggedIn) {
                    if (!isLoggedIn) {
                        authReturnRoute = AppRoutes.RESERVATIONS
                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(AppRoutes.RESERVATIONS) { inclusive = true }
                        }
                    }
                }

                if (isLoggedIn) {
                    ReservationsScreen(
                        reservations = reservations,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onDeleteClick = { reservation ->
                            reservationViewModel.deleteReservation(reservation)
                        }
                    )
                }
            }

            composable(AppRoutes.LOGIN) {
                LoginScreen(
                    onLoginClick = { email, password ->
                        authViewModel.checkUserExists(email) { exists ->
                            if (!exists) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "This email is not registered. Please sign up first."
                                    )
                                }
                                navController.navigate(AppRoutes.SIGNUP)
                            } else {
                                authViewModel.login(email, password) { success ->
                                    if (success) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Login successful")
                                        }
                                        navigateAfterAuth()
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "Wrong email or password"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    onGoToSignUp = {
                        navController.navigate(AppRoutes.SIGNUP)
                    }
                )
            }

            composable(AppRoutes.SIGNUP) {
                SignUpScreen(
                    onSignUpClick = { email, password ->
                        authViewModel.checkUserExists(email) { exists ->
                            if (exists) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "This email already exists. Please log in."
                                    )
                                }
                                navController.navigate(AppRoutes.LOGIN) {
                                    popUpTo(AppRoutes.SIGNUP) { inclusive = true }
                                }
                            } else {
                                authViewModel.signUp(email, password) { success ->
                                    if (success) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "Sign up successful"
                                            )
                                        }
                                        navigateAfterAuth()
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "Sign up failed"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    onGoToLogin = {
                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(AppRoutes.SIGNUP) { inclusive = true }
                        }
                    }
                )
            }
        }

        SnackbarHost(hostState = snackbarHostState)
    }
}