package com.advancedsolutionsdevelopers.cryptomonitor.domain.models

import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Currency

public data class SettingsData(
    val isFirstLaunch: Boolean = true,
    val notificationsEnabled: Boolean = false,
    val currency: Currency = Currency.RUB
)