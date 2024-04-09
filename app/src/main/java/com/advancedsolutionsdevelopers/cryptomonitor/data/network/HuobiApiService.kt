package com.advancedsolutionsdevelopers.cryptomonitor.data.network

import com.advancedsolutionsdevelopers.cryptomonitor.data.models.HuobiResponse
import retrofit2.Response
import retrofit2.http.GET

//интерфейс работы с сервером Huobi
interface HuobiApiService {

    @GET("/market/tickers")
    suspend fun getCoinsPrices(): Response<HuobiResponse>
}