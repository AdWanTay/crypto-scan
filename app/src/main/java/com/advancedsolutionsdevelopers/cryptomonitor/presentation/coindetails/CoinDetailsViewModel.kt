package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coindetails

import com.advancedsolutionsdevelopers.cryptomonitor.core.BaseViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.core.StoreConfig
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.IViewModelFactory
import com.advancedsolutionsdevelopers.cryptomonitor.domain.repository.SettingsRepository
import com.advancedsolutionsdevelopers.cryptomonitor.domain.usecase.BybitQuotesUseCase
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.NavigationChannel
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.NavigationDestination
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist.CoinsListState
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CoinDetailsViewModel @AssistedInject constructor(
    storeConfig: StoreConfig,
    private val bybitQuotesUseCase: BybitQuotesUseCase,
    private val navigationChannel: NavigationChannel,
) : BaseViewModel<CoinDetailsState, CoinDetailsEvent>(storeConfig, CoinDetailsState.Initial) {

    override fun handleEvent(event: CoinDetailsEvent) {
        when (event) {
            is CoinDetailsEvent.OnBackClick -> handleBackClick()
            is CoinDetailsEvent.OnArgsReceived -> handleArgsReceived(event.coinName)
        }
    }

    private fun handleArgsReceived(coinName: String) = intent {
        reduce { _ ->
            CoinDetailsState.Data(
                coinName = coinName,
                minPrice = 0.0,
                maxPrice = 0.0,
                volume = 0.0,
                currentPrice = 0.0,
            )
        }
        val result = bybitQuotesUseCase.execute(Unit).getOrNull()
        if (result != null) {
            for (coin in result) {
                if (coin.type.name == coinName) {
                    reduce { oldState ->
                        when (oldState) {
                            is CoinDetailsState.Data -> {
                                oldState.copy(
                                    minPrice = coin.minPrice,
                                    maxPrice = coin.maxPrice,
                                    volume = coin.volume,
                                    currentPrice = coin.price,
                                )
                            }

                            is CoinDetailsState.Initial -> CoinDetailsState.Data(
                                coinName = coinName,
                                minPrice = coin.minPrice,
                                maxPrice = coin.maxPrice,
                                volume = coin.volume,
                                currentPrice = coin.price
                            )
                        }
                    }
                }
            }
        }
    }

    private fun handleBackClick() = intent {
        navigationChannel.emitDestinationData(NavigationDestination.Back)
    }

    @AssistedFactory
    interface Factory : IViewModelFactory<CoinDetailsViewModel>
}