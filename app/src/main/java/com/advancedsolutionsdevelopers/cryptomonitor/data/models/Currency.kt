package com.advancedsolutionsdevelopers.cryptomonitor.data.models

public enum class Currency(val symbol: String, val marketName: String) {
    USD("$", "USDT"), EUR("€", "EUR"), RUB("₽", "RUB")
}