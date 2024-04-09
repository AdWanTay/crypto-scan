package com.advancedsolutionsdevelopers.cryptomonitor.domain.usecase

import android.util.Log
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineDispatchers
import com.advancedsolutionsdevelopers.cryptomonitor.core.usecase.UseCase
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.CoinItem
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Market
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Market.BINANCE
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Market.BYBIT
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Market.HUOBI
import com.advancedsolutionsdevelopers.cryptomonitor.domain.repository.QuotesRepository
import javax.inject.Inject
import kotlin.math.min

class UpdateQuotesUseCase @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    private val quotesRepository: QuotesRepository,
    private val binanceQuotesUseCase: BinanceQuotesUseCase,
    private val bybitQuotesUseCase: BybitQuotesUseCase,
    private val huobiQuotesUseCase: HuobiQuotesUseCase

) : UseCase<Unit, List<CoinItem>>(coroutineDispatchers.io) {

    override suspend fun run(params: Unit): List<CoinItem> {
        val bybitQuotes = bybitQuotesUseCase.run(params)
        val binanceQuotes = binanceQuotesUseCase.run(params)
        val huobiQuotes = huobiQuotesUseCase.run(params)
        quotesRepository.reduce {//todo
            findMinPrice(
                binanceQuotes.getOrDefault(listOf()),
                bybitQuotes.getOrDefault(listOf()),
                huobiQuotes.getOrDefault(listOf())
            )
        }
        return quotesRepository.stateFlow.value
    }

    private fun findMinPrice(
        binanceQuotes: List<CoinItem>,
        bybitQuotes: List<CoinItem>,
        huobiQuotes: List<CoinItem>
    ): List<CoinItem> {

        val coinsMinPrices = mutableMapOf<Coin, Pair<Double, Market>>()
        for (i in binanceQuotes) {
            coinsMinPrices[i.type] = Pair(i.price, BINANCE)
        }
        for (i in bybitQuotes) {
            if (i.type in coinsMinPrices.keys) {
                if(i.price < coinsMinPrices[i.type]!!.first)
                    coinsMinPrices[i.type] = Pair(i.price, BYBIT)
             } else {
                coinsMinPrices[i.type] = Pair(i.price, BYBIT)
            }
        }
         for (i in huobiQuotes) {
            if (i.type in coinsMinPrices.keys) {
                if(i.price < coinsMinPrices[i.type]!!.first)
                    coinsMinPrices[i.type] = Pair(i.price, HUOBI)
            } else {
                coinsMinPrices[i.type] = Pair(i.price, HUOBI)
            }
        }
        Log.d("UpdateQuotesUseCase", coinsMinPrices.map { coinWithPrice ->
            CoinItem(
                coinWithPrice.key,
                coinWithPrice.value.first,
                coinWithPrice.value.second
            )}.toString())
        return coinsMinPrices.map { coinWithPrice ->
            CoinItem(
                coinWithPrice.key,
                coinWithPrice.value.first,
                coinWithPrice.value.second
            )
        }
    }
}