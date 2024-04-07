package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.advancedsolutionsdevelopers.cryptomonitor.R

class CoinsListAdapter : ListAdapter<CoinListItem, CoinItemViewHolder>(CoinsDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CoinItemViewHolder(layoutInflater.inflate(viewType, parent, false))
    }

    override fun onBindViewHolder(holder: CoinItemViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    override fun getItemViewType(position: Int): Int = R.layout.coins_list_item
}