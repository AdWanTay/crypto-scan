package com.advancedsolutionsdevelopers.cryptomonitor.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CoinPairDto(
    val symbol: String,
    val price: String
)