package com.advancedsolutionsdevelopers.cryptomonitor.domain.usecase

import android.util.Log
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineDispatchers
import com.advancedsolutionsdevelopers.cryptomonitor.core.usecase.BackendUseCase
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.CoinItem
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.BinanceCoinPairDto
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Market
import com.advancedsolutionsdevelopers.cryptomonitor.data.network.BinanceApiService
import com.advancedsolutionsdevelopers.cryptomonitor.data.network.EmptyBodyException
import com.advancedsolutionsdevelopers.cryptomonitor.domain.cache.SettingsCache
import javax.inject.Inject

class BinanceQuotesUseCase @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    private val api: BinanceApiService,
    private val settingsCache: SettingsCache
) :
    BackendUseCase<Unit, Unit, List<BinanceCoinPairDto>, List<CoinItem>>(coroutineDispatchers) {

    override fun transformRequest(params: Unit) {}

    override suspend fun performRequest(request: Unit): Result<List<BinanceCoinPairDto>> {
        val body = api.getCoinsPrices().body()
        Log.d("BinanceQuotesUseCase", body.toString())
        return if (body == null) {
            Result.failure(EmptyBodyException)
        } else {
            Result.success(body)
        }
    }

    override fun transformResponse(response: List<BinanceCoinPairDto>): List<CoinItem> {
        val currencyName = settingsCache.get().currency.marketName
        val coinNames = Coin.entries.toTypedArray().map { coin -> coin.name }
        val concatenations = mutableSetOf<String>()
        for (i in coinNames) {
            concatenations.add(currencyName + i)
            concatenations.add(i + currencyName)
        }
        val result = mutableListOf<CoinItem>()
        for (i in response) {
            if (i.symbol in concatenations) {
                result.add(
                    CoinItem(
                        type = Coin.valueOf(i.symbol.dropCurrency(currencyName)),
                        price = i.price.toDouble(),
                        market = Market.BINANCE

                    )
                )
            }
        }
        return result
    }

    private fun String.dropCurrency(currencyName: String): String {
        return replace(currencyName, "")
    }
}