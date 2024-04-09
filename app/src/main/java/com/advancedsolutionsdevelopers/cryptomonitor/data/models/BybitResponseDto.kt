package com.advancedsolutionsdevelopers.cryptomonitor.data.models

import kotlinx.serialization.Serializable

@Serializable
data class BybitResponseDto(
    val result: BybitResultDto,
)
@Serializable
data class BybitResultDto(
    val list: List<BybitCoinPairDto>
)