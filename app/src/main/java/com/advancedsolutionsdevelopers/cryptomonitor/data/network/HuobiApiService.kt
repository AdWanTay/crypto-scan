package com.advancedsolutionsdevelopers.cryptomonitor.data.network

import retrofit2.Response
import retrofit2.http.GET

//интерфейс работы с сервером Huobi
interface HuobiApiService {

    @GET("list")//TODO
    suspend fun getCoinsList(): Response<Unit>
}