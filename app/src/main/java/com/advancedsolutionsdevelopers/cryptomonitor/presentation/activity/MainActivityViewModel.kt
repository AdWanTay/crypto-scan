package com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.advancedsolutionsdevelopers.cryptomonitor.CONST.COIN_PRICE_ARG
import com.advancedsolutionsdevelopers.cryptomonitor.CONST.COIN_TYPE_ARG
import com.advancedsolutionsdevelopers.cryptomonitor.CONST.CURRENCY_ARG
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.IViewModelFactory
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Currency
import com.advancedsolutionsdevelopers.cryptomonitor.domain.models.NotificationsData
import com.advancedsolutionsdevelopers.cryptomonitor.domain.repository.NotificationsRepository
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist.NotificationDialog
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class MainActivityViewModel @AssistedInject constructor(
    private val navigationChannel: NavigationChannel,
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {

    private var _navController: NavController? = null
    private var _supportFragmentManager: FragmentManager? = null

    init {
        initViewModel()
    }

    fun bindNavController(navController: NavController) {
        _navController = navController
    }

    fun bindFragmentManager(fragmentManager: FragmentManager) {
        _supportFragmentManager = fragmentManager
    }

    fun unbindNavController() {
        _navController = null
    }

    fun unbindFragmentManager() {
        _supportFragmentManager = null
    }

    fun deleteNotification(coin: Coin) {
        viewModelScope.launch {
            notificationsRepository.reduce { oldState ->
                NotificationsData(notifications = oldState.notifications.filterKeys { coinKey -> coinKey != coin })
            }
        }
    }

    fun setNotification(coin: Coin, price: Double) {
        viewModelScope.launch {
            notificationsRepository.reduce { oldState ->
                NotificationsData(notifications = oldState.notifications.filterKeys { coinKey -> coinKey != coin } + mapOf(coin to price))
            }
        }
    }

    private fun initViewModel() {
        consumeDestinations()
    }

    private fun consumeDestinations() {
        viewModelScope.launch {
            navigationChannel.getDestinationsFlow().collect { destination ->
                when (destination) {
                    is NavigationDestination.Back -> _navController?.popBackStack()
                    is NavigationDestination.PathDestination -> navigateToDestination(destination)
                    is NavigationDestination.NotificationDialog -> openNotificationDialog(destination.coin, destination.price, destination.currency)

                }
            }
        }
    }

    private fun navigateToDestination(destination: NavigationDestination.PathDestination) {
        val args = Bundle().apply {
            destination.args?.forEach { (key, value) ->
                putString(key, value)
            }
        }
        _navController?.navigate(destination.path, args)
    }

    private fun openNotificationDialog(coin: Coin, price: Double, currency: Currency) {
        _supportFragmentManager?.let { fragmentManager ->
            val dialog = NotificationDialog()
            dialog.arguments = Bundle().apply {
                putString(COIN_TYPE_ARG, coin.name)
                putString(CURRENCY_ARG, currency.name)
                putDouble(COIN_PRICE_ARG, price)
            }
            dialog.show(fragmentManager, "NOTIFICATION_DIALOG")
        }
    }

    @AssistedFactory
    interface Factory : IViewModelFactory<MainActivityViewModel>
}