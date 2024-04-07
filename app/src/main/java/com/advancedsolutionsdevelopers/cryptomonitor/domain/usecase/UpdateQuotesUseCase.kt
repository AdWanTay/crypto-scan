package com.advancedsolutionsdevelopers.cryptomonitor.domain.usecase

import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineDispatchers
import com.advancedsolutionsdevelopers.cryptomonitor.core.usecase.UseCase
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.CoinItem
import com.advancedsolutionsdevelopers.cryptomonitor.domain.repository.QuotesRepository
import javax.inject.Inject
import kotlin.math.min

class UpdateQuotesUseCase @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    private val quotesRepository: QuotesRepository,
    private val binanceQuotesUseCase: BinanceQuotesUseCase
) : UseCase<Unit, List<CoinItem>>(coroutineDispatchers.io) {

    override suspend fun run(params: Unit): List<CoinItem> {
        val binanceQuotes = binanceQuotesUseCase.run(params)
        quotesRepository.reduce {//todo
            findMinPrice(binanceQuotes.getOrDefault(listOf()), binanceQuotes.getOrDefault(listOf()), binanceQuotes.getOrDefault(listOf()))
        }
        return quotesRepository.stateFlow.value
    }

    private fun findMinPrice(binanceQuotes: List<CoinItem>, bybitQuotes: List<CoinItem>, huobiQuotes: List<CoinItem>): List<CoinItem> {
        val coinsMinPrices = mutableMapOf<Coin, Double>()
        for (i in binanceQuotes) {
            coinsMinPrices[i.type] = i.price
        }
        for (i in bybitQuotes) {
            if (i.type in coinsMinPrices.keys) {
                coinsMinPrices[i.type] = min(i.price, coinsMinPrices[i.type]!!)
            }
        }
        for (i in huobiQuotes) {
            if (i.type in coinsMinPrices.keys) {
                coinsMinPrices[i.type] = min(i.price, coinsMinPrices[i.type]!!)
            }
        }
        return coinsMinPrices.map { coinWithPrice -> CoinItem(coinWithPrice.key, coinWithPrice.value) }
    }
}