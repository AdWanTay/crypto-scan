package com.advancedsolutionsdevelopers.cryptomonitor.data.network

import android.net.ConnectivityManager
import android.net.Network

//коллбек для контроля состояния сети
class NetCallback(
) :
    ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
    }

    private fun isOnline(): Boolean {
/*        val capabilities =
            repository.connectManager
                .getNetworkCapabilities(repository.connectManager.activeNetwork)
        if (capabilities != null) {
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        }*/
        return false
    }
}