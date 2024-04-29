package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coindetails

sealed interface CoinDetailsEvent {
    data object OnBackClick: CoinDetailsEvent

    class OnArgsReceived(val coinName: String) : CoinDetailsEvent
}