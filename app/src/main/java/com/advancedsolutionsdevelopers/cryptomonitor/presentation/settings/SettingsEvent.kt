package com.advancedsolutionsdevelopers.cryptomonitor.presentation.settings

import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Currency

sealed interface SettingsEvent {
    class CurrencySelected(val selectedCurrency: Currency): SettingsEvent
    data object OnBackClick: SettingsEvent
    data object OnResetClick: SettingsEvent
}