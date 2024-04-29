package com.advancedsolutionsdevelopers.cryptomonitor.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BybitCoinPairDto (
    val symbol: String,
    @SerialName("indexPrice") val price: String,
    @SerialName("lowPrice24h") val minPrice: String,
    @SerialName("highPrice24h") val maxPrice: String,
    @SerialName("volume24h") val volume: String,
)
