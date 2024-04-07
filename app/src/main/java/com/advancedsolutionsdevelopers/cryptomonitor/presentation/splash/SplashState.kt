package com.advancedsolutionsdevelopers.cryptomonitor.presentation.splash

data class SplashState(val notificationsButtonState: NotificationsButtonState = NotificationsButtonState.INITIAL)

enum class NotificationsButtonState {
    INITIAL, GRANTED, DENIED
}