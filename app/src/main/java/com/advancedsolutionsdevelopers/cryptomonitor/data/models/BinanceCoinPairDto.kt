package com.advancedsolutionsdevelopers.cryptomonitor.data.models

import kotlinx.serialization.Serializable

@Serializable
data class BinanceCoinPairDto(
    val symbol: String,
    val price: String
)
