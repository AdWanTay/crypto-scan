package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist

sealed interface CoinsListEvent {
    data object OnSettingsClick : CoinsListEvent
    data object ViewCreated : CoinsListEvent
}