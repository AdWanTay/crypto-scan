package com.advancedsolutionsdevelopers.cryptomonitor.data.network

import com.advancedsolutionsdevelopers.cryptomonitor.data.models.BybitResponseDto
import retrofit2.Response
import retrofit2.http.GET

//интерфейс работы с сервером Bybit
interface BybitApiService {
    @GET("/v5/market/tickers?category=linear")
    suspend fun getCoinsPrices(): Response<BybitResponseDto>
}