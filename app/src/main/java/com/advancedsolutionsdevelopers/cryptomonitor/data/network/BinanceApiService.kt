package com.advancedsolutionsdevelopers.cryptomonitor.data.network

import com.advancedsolutionsdevelopers.cryptomonitor.data.models.BinanceCoinPairDto
import retrofit2.Response
import retrofit2.http.GET

//интерфейс работы с сервером Binance
interface BinanceApiService {
    @GET("/api/v3/ticker/price")
    suspend fun getCoinsPrices(): Response<List<BinanceCoinPairDto>>
}