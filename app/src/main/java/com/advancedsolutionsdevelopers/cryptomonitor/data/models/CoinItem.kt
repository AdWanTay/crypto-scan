package com.advancedsolutionsdevelopers.cryptomonitor.data.models

public data class CoinItem(val type: Coin = Coin.USDT, val price: Double = .0, val market: Market = Market.BINANCE, var minPrice: Double = .0, var maxPrice: Double = .0, var volume: Double = .0)