package com.advancedsolutionsdevelopers.cryptomonitor.domain.usecase

import android.util.Log
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineDispatchers
import com.advancedsolutionsdevelopers.cryptomonitor.core.usecase.BackendUseCase
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.BybitCoinPairDto
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.CoinItem
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Market
import com.advancedsolutionsdevelopers.cryptomonitor.data.network.BybitApiService
import com.advancedsolutionsdevelopers.cryptomonitor.data.network.EmptyBodyException
import com.advancedsolutionsdevelopers.cryptomonitor.domain.cache.SettingsCache
import javax.inject.Inject

class BybitQuotesUseCase @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    private val api: BybitApiService,
    private val settingsCache: SettingsCache
) :
    BackendUseCase<Unit, Unit, List<BybitCoinPairDto>, List<CoinItem>>(coroutineDispatchers) {
    override fun transformRequest(params: Unit) {
    }

    override suspend fun performRequest(request: Unit): Result<List<BybitCoinPairDto>> {
        val body = api.getCoinsPrices().body()
        return if (body == null) {
            Result.failure(EmptyBodyException)
        } else {
            Result.success(body.result.list)
        }
    }

    override fun transformResponse(response: List<BybitCoinPairDto>): List<CoinItem> {
        val currencyName = settingsCache.get().currency.marketName
        val coinNames = Coin.entries.toTypedArray().map { coin -> coin.name }
        val USDTname = "USDT"

        val concatenationsUSDT = mutableSetOf<String>()
        val concatenations = mutableSetOf<String>()
        for (i in coinNames) {
            concatenations.add(currencyName + i)
            concatenations.add(i + currencyName)
            concatenationsUSDT.add(USDTname + i)
            concatenationsUSDT.add(i + USDTname)
        }
        val result = mutableListOf<CoinItem>()
        val resultUSDT = mutableListOf<CoinItem>()

        for (i in response) {
            if (i.symbol in concatenations) {
                result.add(
                    CoinItem(
                        type = Coin.valueOf(i.symbol.dropCurrency(currencyName)),
                        price = i.price.toDouble(),
                        market = Market.BYBIT,
                    )
                )
            }
        }

        for (i in response) {
            if (i.symbol in concatenationsUSDT) {
                resultUSDT.add(
                    CoinItem(
                        type = Coin.valueOf(i.symbol.dropCurrency(USDTname)),
                        price = i.price.toDouble(),
                        market = Market.BYBIT,
                        minPrice = i.minPrice.toDouble(),
                        maxPrice = i.maxPrice.toDouble(),
                        volume = i.volume.toDouble()
                    )
                )
            }
        }
//        Log.d("BybitQuotesUseCase", resultUSDT.toString())

        var f = false
        for (ur in resultUSDT) {
            f = false
            for (r in result) {
                if (ur.type == r.type) {
                    r.minPrice = ur.minPrice
                    r.maxPrice = ur.maxPrice
                    r.volume = ur.volume
                    f = true
                }
            }
            if (!f) {
                result.add(
                    CoinItem(
                        type = ur.type,
                        market = ur.market,
                        minPrice = ur.minPrice,
                        maxPrice = ur.maxPrice,
                        volume = ur.volume
                    )
                )
            }
        }
        Log.d("BybitQuotesUseCase", result.toString())
        return result
    }

    private fun String.dropCurrency(currencyName: String): String {
        return replace(currencyName, "")
    }
}