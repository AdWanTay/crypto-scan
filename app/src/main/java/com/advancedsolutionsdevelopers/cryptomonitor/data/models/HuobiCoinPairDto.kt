package com.advancedsolutionsdevelopers.cryptomonitor.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HuobiCoinPairDto(
    val symbol: String,
    val bid : Double,
    val price: String = bid.toString()
)
@Serializable
data class HuobiResponse(
    val data: List<HuobiCoinPairDto>
)