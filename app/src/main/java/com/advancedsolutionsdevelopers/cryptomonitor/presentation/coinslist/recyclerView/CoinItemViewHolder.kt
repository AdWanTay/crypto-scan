package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist.recyclerView

import android.annotation.SuppressLint
import android.view.View
import android.view.View.GONE
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.advancedsolutionsdevelopers.cryptomonitor.R
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.PriceTrend
import com.advancedsolutionsdevelopers.cryptomonitor.databinding.CoinsListItemBinding
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.format

class CoinItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding by lazy { CoinsListItemBinding.bind(itemView) }

    @SuppressLint("SetTextI18n")
    fun onBind(item: CoinListItem) = with(binding) {
        coinIconImageView.setImageResource(item.coinLogo)
        coinNameTextView.text = item.coinName()
        coinPriceTextView.text = (item.price?.format() ?: "- ") + item.currency.symbol
        if (item.price?.format().isNullOrBlank())
            marketTextView.text = ""
        else
            marketTextView.text = item.coinMarket.name

        marketTextView.text = item.coinMarket.name
        coinPriceTextView.setTextColorByTrend(item.priceTrend)
        root.isClickable = true
        root.setOnClickListener { item.onItemClickCallback() }
        coinPriceNotification.setImageResource(
            if (item.areNotificationsEnabled) R.drawable.ic_notifications_on
            else R.drawable.ic_notifications_off
        )
        coinPriceNotification.setOnClickListener { item.onRingClickCallback() }
    }

    private fun TextView.setTextColorByTrend(trend: PriceTrend) {
        val color = context.getColor(
            when (trend) {
                PriceTrend.STRAIGHT -> R.color.base_milky
                PriceTrend.DOWN -> R.color.base_red
                PriceTrend.UP -> R.color.base_green
            }
        )
        setTextColor(color)
    }
}