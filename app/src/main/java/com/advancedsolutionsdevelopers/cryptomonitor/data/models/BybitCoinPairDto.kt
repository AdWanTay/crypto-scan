package com.advancedsolutionsdevelopers.cryptomonitor.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BybitCoinPairDto (
    val symbol: String,
    @SerialName("indexPrice") val price: String
)
