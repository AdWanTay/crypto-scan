package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist.recyclerView

import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Currency
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Market
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.PriceTrend
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinName

public data class CoinListItem(
    val coinType: Coin,
    val coinLogo: Int,
    val coinMarket: Market,
    val price: Double?,
    val priceTrend: PriceTrend,
    val currency: Currency,
    val areNotificationsEnabled: Boolean,
    val onItemClickCallback: () -> Unit,
    val onRingClickCallback: () -> Unit
) {
    fun coinName() = coinType.coinName()
}