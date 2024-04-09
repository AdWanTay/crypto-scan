package com.advancedsolutionsdevelopers.cryptomonitor.di.module

import com.advancedsolutionsdevelopers.cryptomonitor.core.di.ApplicationScope
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.qualifiers.Binance
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.qualifiers.Bybit
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.qualifiers.Huobi
import com.advancedsolutionsdevelopers.cryptomonitor.data.network.BinanceApiService
import com.advancedsolutionsdevelopers.cryptomonitor.data.network.BybitApiService
import com.advancedsolutionsdevelopers.cryptomonitor.data.network.HuobiApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Module
interface NetworkModule {
    companion object {
        @Provides
        @ApplicationScope
        @Binance
        fun provideBinanceApiUrl(): String = "https://api.binance.com"

        @Provides
        @ApplicationScope
        @Bybit
        fun provideBybitApiUrl(): String = "https://api-testnet.bybit.com"

        @Provides
        @ApplicationScope
        @Huobi
        fun provideHuobiApiUrl(): String = "https://api.huobi.pro"

        @Provides
        @ApplicationScope
        fun provideNetworkClient(): OkHttpClient {
            return OkHttpClient().newBuilder().callTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build()
        }


        @Provides
        @ApplicationScope
        @Binance
        fun provideBinanceRetrofit(
            @Binance url: String,
            client: OkHttpClient
        ): Retrofit = Retrofit.Builder().baseUrl(url).client(client)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()

        @Provides
        @ApplicationScope
        @Bybit
        fun provideBybitRetrofit(
            @Bybit url: String,
            client: OkHttpClient
        ): Retrofit = Retrofit.Builder().apply {
            baseUrl(url)
            client(client)
            val json = Json { ignoreUnknownKeys = true }
            addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        }.build()

        @Provides
        @ApplicationScope
        @Huobi
        fun provideHuobiRetrofit(
            @Huobi url: String,
            client: OkHttpClient
        ): Retrofit =  Retrofit.Builder().apply {
            baseUrl(url)
            client(client)
            val json = Json { ignoreUnknownKeys = true }
            addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        }.build()


        @Provides
        @ApplicationScope
        fun provideBinanceApi(@Binance retrofit: Retrofit): BinanceApiService =
            retrofit.create(BinanceApiService::class.java)

        @Provides
        @ApplicationScope
        fun provideBybitApi(@Bybit retrofit: Retrofit): BybitApiService =
            retrofit.create(BybitApiService::class.java)

        @Provides
        @ApplicationScope
        fun provideHuobiApi(@Huobi retrofit: Retrofit): HuobiApiService =
            retrofit.create(HuobiApiService::class.java)
    }
}