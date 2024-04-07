package com.advancedsolutionsdevelopers.cryptomonitor.presentation.splash

import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Currency

sealed interface SplashEvent {
    class CurrencySelected(val selectedCurrency: Currency): SplashEvent
    data object OnNextClick: SplashEvent
    data object NotificationsGranted: SplashEvent
    data object NotificationsDenied: SplashEvent
}