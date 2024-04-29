package com.advancedsolutionsdevelopers.cryptomonitor.presentation.settings

import com.advancedsolutionsdevelopers.cryptomonitor.core.BaseViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.core.StoreConfig
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.IViewModelFactory
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Currency
import com.advancedsolutionsdevelopers.cryptomonitor.domain.models.NotificationsData
import com.advancedsolutionsdevelopers.cryptomonitor.domain.models.SettingsData
import com.advancedsolutionsdevelopers.cryptomonitor.domain.repository.NotificationsRepository
import com.advancedsolutionsdevelopers.cryptomonitor.domain.repository.SettingsRepository
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.NavigationChannel
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.NavigationDestination
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SettingsViewModel @AssistedInject constructor(
    storeConfig: StoreConfig,
    private val settingsRepository: SettingsRepository,
    private val notificationsRepository: NotificationsRepository,
    private val navigationChannel: NavigationChannel
) : BaseViewModel<SettingsState, SettingsEvent>(storeConfig, initialState = SettingsState(Currency.RUB, 2.0)) {

    init {
        initViewModel()
    }

    private fun initViewModel() {
        updateSelectedCurrency()
        updateSelectedTimeInterval()
    }

    private fun updateSelectedCurrency() = intent {
        reduce { settingsState ->
            settingsState.copy(
                selectedCurrency = settingsRepository.stateFlow.value.currency
            )
        }
    }

    private fun updateSelectedTimeInterval() = intent {
        reduce { settingsState ->
            settingsState.copy(
                timeInterval = settingsRepository.stateFlow.value.timeInterval
            )
        }
    }

    override fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.CurrencySelected -> handleCurrencySelection(event.selectedCurrency)
            is SettingsEvent.TimeIntervalSelected -> handleTimeIntervalSelection(event.selectedTimeInterval)
            is SettingsEvent.OnBackClick -> handleBackClick()
            is SettingsEvent.OnResetClick -> handleReset()
        }
    }

    private fun handleBackClick() = intent {
        navigationChannel.emitDestinationData(NavigationDestination.Back)
    }

    private fun handleReset() = intent {
        settingsRepository.reduce {
            SettingsData()
        }
        notificationsRepository.reduce {
            NotificationsData()
        }
        navigationChannel.emitDestinationData(NavigationDestination.Back)
    }

    private fun handleCurrencySelection(newCurrency: Currency) = intent {
        settingsRepository.reduce { oldState ->
            oldState.copy(currency = newCurrency)
        }
        notificationsRepository.reduce {
            //Because we don't know what's price user want to achieve right now
            NotificationsData()
        }
    }

    private fun handleTimeIntervalSelection(newTimeInterval: Double) = intent {
        settingsRepository.reduce { oldState ->
            oldState.copy(timeInterval = newTimeInterval)
        }
        notificationsRepository.reduce {
            //Because we don't know what's price user want to achieve right now
            NotificationsData()
        }
    }


    @AssistedFactory
    interface Factory : IViewModelFactory<SettingsViewModel>
}