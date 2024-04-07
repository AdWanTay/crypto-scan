package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist

import com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist.recyclerView.CoinListItem

sealed interface CoinsListState {
    data object Loading : CoinsListState
    data class Data(val coinsList: List<CoinListItem>) : CoinsListState
}
