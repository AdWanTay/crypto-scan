package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist.recyclerView

import androidx.recyclerview.widget.DiffUtil

class CoinsDiffUtilCallback : DiffUtil.ItemCallback<CoinListItem>() {

    override fun areItemsTheSame(oldItem: CoinListItem, newItem: CoinListItem): Boolean {
        return oldItem.coinType == newItem.coinType
    }

    override fun areContentsTheSame(oldItem: CoinListItem, newItem: CoinListItem): Boolean {
        return oldItem.price == newItem.price &&
                oldItem.currency == newItem.currency &&
                oldItem.coinMarket == newItem.coinMarket &&
                oldItem.areNotificationsEnabled == newItem.areNotificationsEnabled
    }
}