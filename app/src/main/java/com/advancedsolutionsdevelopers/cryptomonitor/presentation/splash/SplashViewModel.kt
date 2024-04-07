package com.advancedsolutionsdevelopers.cryptomonitor.presentation.splash

import com.advancedsolutionsdevelopers.cryptomonitor.core.BaseViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.core.StoreConfig
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.IViewModelFactory
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Currency
import com.advancedsolutionsdevelopers.cryptomonitor.domain.repository.SettingsRepository
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.NavigationChannel
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.NavigationDestination
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SplashViewModel @AssistedInject constructor(
    storeConfig: StoreConfig,
    private val settingsRepository: SettingsRepository,
    private val navigationChannel: NavigationChannel
) : BaseViewModel<SplashState, SplashEvent>(storeConfig, initialState = SplashState()) {

    init {
        initViewModel()
    }

    private fun initViewModel() {
        settingsRepository.reduce { oldState ->
            oldState.copy(isFirstLaunch = false)
        }
    }

    override fun handleEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.CurrencySelected -> handleCurrencySelection(event.selectedCurrency)
            is SplashEvent.OnNextClick -> handleBackClick()
            is SplashEvent.NotificationsDenied -> handleNotificationsPermChange(false)
            is SplashEvent.NotificationsGranted -> handleNotificationsPermChange(true)
        }
    }

    private fun handleBackClick() = intent {
        navigationChannel.emitDestinationData(NavigationDestination.Back)
    }

    private fun handleCurrencySelection(newCurrency: Currency) = intent {
        settingsRepository.reduce { oldState ->
            oldState.copy(currency = newCurrency)
        }
    }

    private fun handleNotificationsPermChange(isGranted: Boolean) = intent {
        settingsRepository.reduce { oldState ->
            oldState.copy(notificationsEnabled = isGranted)
        }
        intent {
            reduce { oldState ->
                oldState.copy(notificationsButtonState = if (isGranted) NotificationsButtonState.GRANTED else NotificationsButtonState.DENIED)
            }
        }
    }

    @AssistedFactory
    interface Factory : IViewModelFactory<SplashViewModel>
}