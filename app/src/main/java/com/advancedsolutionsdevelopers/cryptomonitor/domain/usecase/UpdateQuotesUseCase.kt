package com.advancedsolutionsdevelopers.cryptomonitor.domain.usecase

import com.advancedsolutionsdevelopers.cryptomonitor.CONST
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineDispatchers
import com.advancedsolutionsdevelopers.cryptomonitor.core.usecase.BackendUseCase
import com.advancedsolutionsdevelopers.cryptomonitor.core.usecase.UseCase
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.CoinItem
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Market
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Market.BINANCE
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Market.BYBIT
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Market.HUOBI
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateQuotesUseCase @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    private val binanceQuotesUseCase: BinanceQuotesUseCase,
    private val bybitQuotesUseCase: BybitQuotesUseCase,
    private val huobiQuotesUseCase: HuobiQuotesUseCase

) : UseCase<Unit, Flow<List<CoinItem>>>(coroutineDispatchers.io) {
    override suspend fun run(params: Unit): Flow<List<CoinItem>> {
        return combine(
            poll(binanceQuotesUseCase, params),
            poll(bybitQuotesUseCase, params),
            poll(huobiQuotesUseCase, params)
        ) { binance, bybit, huobi ->
            findMinPrice(binance, bybit, huobi)
        }
    }

    private fun <I> poll(
        useCase: BackendUseCase<Unit, Unit, I, List<CoinItem>>,
        params: Unit
    ): Flow<List<CoinItem>> = flow {
        while (true) {
            val result = useCase.run(params)
            emit(result.getOrDefault(listOf()))
            delay(CONST.REQUEST_DELAY)
        }
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
                if (i.price < coinsMinPrices[i.type]!!.first)
                    coinsMinPrices[i.type] = Pair(i.price, BYBIT)
            } else {
                coinsMinPrices[i.type] = Pair(i.price, BYBIT)
            }
        }
        val bybitMap = mutableMapOf<Coin,CoinItem>()
        for (i in bybitQuotes){
            bybitMap[i.type] = i
        }
        for (i in huobiQuotes) {
            if (i.type in coinsMinPrices.keys) {
                if (i.price < coinsMinPrices[i.type]!!.first)
                    coinsMinPrices[i.type] = Pair(i.price, HUOBI)
            } else {
                coinsMinPrices[i.type] = Pair(i.price, HUOBI)
            }
        }

        return coinsMinPrices.map { coinWithPrice ->
            CoinItem(
                type = coinWithPrice.key,
                price = coinWithPrice.value.first,
                market = coinWithPrice.value.second,
                minPrice = bybitMap[coinWithPrice.key]?.minPrice?: 0.0,
                maxPrice = bybitMap[coinWithPrice.key]?.maxPrice?: 0.0,
                volume = bybitMap[coinWithPrice.key]?.volume?: 0.0,
            )
        }
    }
}