package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coindetails

import com.advancedsolutionsdevelopers.cryptomonitor.core.BaseViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.core.StoreConfig
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.IViewModelFactory
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.NavigationChannel
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.NavigationDestination
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CoinDetailsViewModel @AssistedInject constructor(
    storeConfig: StoreConfig,
    private val navigationChannel: NavigationChannel
) : BaseViewModel<CoinDetailsState, CoinDetailsEvent>(storeConfig, CoinDetailsState.Initial) {
    override fun handleEvent(event: CoinDetailsEvent) {
        when (event) {
            CoinDetailsEvent.OnBackClick -> handleBackClick()
        }
    }

    private fun handleBackClick() = intent {
        navigationChannel.emitDestinationData(NavigationDestination.Back)
    }

    @AssistedFactory
    interface Factory : IViewModelFactory<CoinDetailsViewModel>
}