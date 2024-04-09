package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist

import com.advancedsolutionsdevelopers.cryptomonitor.core.BaseViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.core.StoreConfig
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.IViewModelFactory
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.CoinItem
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Currency
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Market
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.PriceTrend
import com.advancedsolutionsdevelopers.cryptomonitor.domain.repository.NotificationsRepository
import com.advancedsolutionsdevelopers.cryptomonitor.domain.repository.QuotesRepository
import com.advancedsolutionsdevelopers.cryptomonitor.domain.repository.SettingsRepository
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.NavigationChannel
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.NavigationDestination
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist.recyclerView.CoinListItem
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.toIconRes
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.combine

class CoinsListViewModel @AssistedInject constructor(
    storeConfig: StoreConfig,
    private val quotesRepository: QuotesRepository,
    private val settingsRepository: SettingsRepository,
    private val notificationsRepository: NotificationsRepository,
    private val navigationChannel: NavigationChannel
) : BaseViewModel<CoinsListState, CoinsListEvent>(storeConfig, initialState = CoinsListState.Loading) {

    init {
        initViewModel()
    }

    private fun initViewModel() {
        observeCoins()
        observeCurrency()
    }

    private fun observeCurrency() = intent {
        settingsRepository.stateFlow.collect { settings ->
            val coinsList = mutableListOf<CoinListItem>()
            val notificationsMap = notificationsRepository.stateFlow.value.notifications
            for (i in Coin.entries) {
                coinsList.add(CoinItem(i, 0.0, Market.BINANCE).stub(settings.currency, notificationsMap))
            }
            reduce {
                CoinsListState.Data(coinsList = coinsList)
            }
        }
    }

    private fun checkIsFirstLaunch() = intent {
        if (settingsRepository.stateFlow.value.isFirstLaunch) {
            navigationChannel.emitDestinationData(NavigationDestination.Splash)
        }
    }

    private fun observeCoins() = intent {
        combine(
            quotesRepository.stateFlow,
            notificationsRepository.stateFlow
        ) { newQuotes, notificationsState ->
            Triple(newQuotes, settingsRepository.stateFlow.value.currency, notificationsState.notifications)
        }.collect { combined ->
            val newQuotes = combined.first
            val currency = combined.second
            val notifications = combined.third
            onNewQuotesAvailable(newQuotes, currency, notifications)
        }
    }

    private fun onNewQuotesAvailable(newQuotes: List<CoinItem>, currency: Currency, notifications: Map<Coin, Double>) = intent {
        reduce { oldState ->
            when (oldState) {
                is CoinsListState.Data -> {
                    val oldCoins = oldState.coinsList
                    CoinsListState.Data(coinsList = oldCoins.updateCoins(newQuotes, currency, notifications))
                }

                is CoinsListState.Loading -> CoinsListState.Data(newQuotes.map { it.toCoinListItem(currency, notifications) })
            }
        }
    }

    override fun handleEvent(event: CoinsListEvent) {
        when (event) {
            CoinsListEvent.OnSettingsClick -> handleSettingsClick()
            CoinsListEvent.ViewCreated -> checkIsFirstLaunch()
        }
    }

    private fun handleSettingsClick() = intent {
        navigationChannel.emitDestinationData(NavigationDestination.Settings)
    }

    private fun List<CoinListItem>.updateCoins(newQuotes: List<CoinItem>, currency: Currency, notifications: Map<Coin, Double>): List<CoinListItem> {
        val coinsMap = mutableMapOf<Coin, CoinListItem>()
        for (i in this) {
            coinsMap[i.coinType] = i
        }
        for (newQuote in newQuotes) {
            if (newQuote.type in coinsMap.keys) {
                val oldState = coinsMap[newQuote.type]!!
                coinsMap.replace(
                    newQuote.type,
                    oldState.copy(
                        currency = currency,
                        coinMarket = newQuote.market,
                        areNotificationsEnabled = newQuote.type in notifications.keys,
                        price = newQuote.price,
                        priceTrend = oldState.price?.let { oldPrice -> calcTrend(oldPrice, newQuote.price) } ?: PriceTrend.STRAIGHT,
                        onRingClickCallback = {
                            onRingClick(
                                type = oldState.coinType,
                                price = if (newQuote.type in notifications.keys) notifications[newQuote.type]!! else newQuote.price,
                                currency = currency
                            )
                        }
                    )
                )
            } else {
                coinsMap[newQuote.type] = newQuote.toCoinListItem(currency, notifications)
            }
        }
        return coinsMap.map { it.value }
    }

    private fun calcTrend(oldPrice: Double, newPrice: Double): PriceTrend {
        val delta = oldPrice - newPrice
        return when {
            delta > 0.0 -> PriceTrend.DOWN
            delta < 0.0 -> PriceTrend.UP
            else -> PriceTrend.STRAIGHT
        }
    }

    private fun CoinItem.toCoinListItem(currency: Currency, notifications: Map<Coin, Double>): CoinListItem {
        return CoinListItem(
            coinType = type,
            coinLogo = type.toIconRes(),
            coinMarket = market,
            price = price,
            priceTrend = PriceTrend.STRAIGHT,
            currency = currency,
            areNotificationsEnabled = type in notifications.keys,
            onItemClickCallback = { onCoinClick(type) },
            onRingClickCallback = { onRingClick(type, price, currency) }
        )
    }

    private fun CoinItem.stub(currency: Currency, notifications: Map<Coin, Double>): CoinListItem {
        return CoinListItem(
            coinType = type,
            coinLogo = type.toIconRes(),
            coinMarket = market,
            price = null,
            priceTrend = PriceTrend.STRAIGHT,
            currency = currency,
            areNotificationsEnabled = type in notifications.keys,
            onItemClickCallback = { onCoinClick(type) },
            onRingClickCallback = { onRingClick(type, 0.0, currency) }
        )
    }

    private fun onRingClick(type: Coin, price: Double, currency: Currency) {
        navigationChannel.emitDestinationData(NavigationDestination.NotificationDialog(type, price, currency))
    }

    private fun onCoinClick(type: Coin) {
        navigationChannel.emitDestinationData(NavigationDestination.CoinDetails(type))
    }

    @AssistedFactory
    interface Factory : IViewModelFactory<CoinsListViewModel>
}