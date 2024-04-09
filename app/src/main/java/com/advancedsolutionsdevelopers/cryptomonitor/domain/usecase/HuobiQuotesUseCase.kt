package com.advancedsolutionsdevelopers.cryptomonitor.domain.usecase

import android.util.Log
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineDispatchers
import com.advancedsolutionsdevelopers.cryptomonitor.core.usecase.BackendUseCase
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.CoinItem
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.HuobiCoinPairDto
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Market
import com.advancedsolutionsdevelopers.cryptomonitor.data.network.EmptyBodyException
import com.advancedsolutionsdevelopers.cryptomonitor.data.network.HuobiApiService
import com.advancedsolutionsdevelopers.cryptomonitor.domain.cache.SettingsCache
import javax.inject.Inject

class HuobiQuotesUseCase @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    private val api: HuobiApiService,
    private val settingsCache: SettingsCache
) :
    BackendUseCase<Unit, Unit, List<HuobiCoinPairDto>, List<CoinItem>>(coroutineDispatchers) {
    override fun transformRequest(params: Unit) {
    }

    override suspend fun performRequest(request: Unit): Result<List<HuobiCoinPairDto>> {
        val body = api.getCoinsPrices().body()
        return if (body == null) {
            Result.failure(EmptyBodyException)
        } else {
            Result.success(body.data)
        }
    }

    override fun transformResponse(response: List<HuobiCoinPairDto>): List<CoinItem> {
        val currencyName = settingsCache.get().currency.marketName
        val coinNames = Coin.entries.toTypedArray().map { coin -> coin.name }
        val concatenations = mutableSetOf<String>()
        for (i in coinNames) {
            concatenations.add(currencyName + i)
            concatenations.add(i + currencyName)
        }
        val result = mutableListOf<CoinItem>()
        for (i in response) {
            if (i.symbol.uppercase() in concatenations) {
                Log.d("HuobiQuotesUseCase",i.price.toDouble().toString())
                result.add(
                    CoinItem(
                        type = Coin.valueOf(i.symbol.uppercase().dropCurrency(currencyName)),
                        price = i.price.toDouble(),
                        market = Market.HUOBI
                    )
                )
            }
        }
        Log.d("HuobiQuotesUseCase", result.toString())
        return result
    }
    private fun String.dropCurrency(currencyName: String): String {
        return replace(currencyName, "")
    }
}