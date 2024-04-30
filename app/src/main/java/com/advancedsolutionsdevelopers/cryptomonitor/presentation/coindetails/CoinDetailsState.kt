package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coindetails

import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.CoinItem
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist.CoinsListState
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist.recyclerView.CoinListItem

sealed interface CoinDetailsState {
    data object Initial: CoinDetailsState
    data class Data(val coinName: String, val currentPrice: Double, val minPrice: Double, val maxPrice: Double, val volume: Double) : CoinDetailsState
}