package com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity

import com.advancedsolutionsdevelopers.cryptomonitor.CONST.COIN_TYPE_ARG
import com.advancedsolutionsdevelopers.cryptomonitor.R
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.ActivityScope
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Currency
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@ActivityScope
public class NavigationChannel @Inject constructor() {
    private val _events = MutableSharedFlow<NavigationDestination>(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    fun emitDestinationData(navigationDestination: NavigationDestination) {
        _events.tryEmit(navigationDestination)
    }

    fun getDestinationsFlow(): SharedFlow<NavigationDestination> = _events.asSharedFlow()
}

public sealed interface NavigationDestination {
    data object Back : NavigationDestination
    data class NotificationDialog(val coin: Coin, val price: Double, val currency: Currency) : NavigationDestination
    abstract class PathDestination(open val path: Int, open val args: Map<String, String>? = null) : NavigationDestination
    object Splash : PathDestination(path = R.id.action_coinsListFragment_to_splashFragment)
    object Settings : PathDestination(path = R.id.action_coinsListFragment_to_settingsFragment)
    data class CoinDetails(val coin: Coin) :
        PathDestination(path = R.id.action_coinsListFragment_to_coinDetailsFragment, args = mapOf(COIN_TYPE_ARG to coin.name))
}
