package com.advancedsolutionsdevelopers.cryptomonitor.presentation

import com.advancedsolutionsdevelopers.cryptomonitor.R
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import kotlin.math.max

fun Coin.toIconRes(): Int {
    return when (this) {
        Coin.USDT -> R.drawable.ic_usdt
        Coin.USDC -> R.drawable.ic_usdc
        Coin.BTC -> R.drawable.ic_btc
        Coin.ETH -> R.drawable.ic_eth
        Coin.LTC -> R.drawable.ic_ltc
        Coin.XMR -> R.drawable.ic_xmr
        Coin.DASH -> R.drawable.ic_dash
        Coin.ZEC -> R.drawable.ic_zec
        Coin.CHZ -> R.drawable.ic_chz
        Coin.DOGE -> R.drawable.ic_doge
        Coin.DGB -> R.drawable.ic_dgb
        Coin.SOL -> R.drawable.ic_sol
        Coin.XRP -> R.drawable.ic_xrp
        Coin.ADA -> R.drawable.ic_ada
        Coin.AAVE -> R.drawable.ic_aave
        Coin.DOT -> R.drawable.ic_dot
        Coin.TRX -> R.drawable.ic_trx
        Coin.MATIC -> R.drawable.ic_matic
        Coin.ETC -> R.drawable.ic_etc
        Coin.BCH -> R.drawable.ic_bch
    }
}

fun Coin.coinName(): String {
    return when (this) {
        Coin.USDT,
        Coin.XRP,
        Coin.AAVE,
        Coin.TRX,
        Coin.USDC -> name

        Coin.BTC -> "Bitcoin"
        Coin.ETH -> "Etherium"
        Coin.LTC -> "Litecoin"
        Coin.XMR -> "Monero"
        Coin.DASH -> "Dash"
        Coin.ZEC -> "Zcash"
        Coin.CHZ -> "Chiliz"
        Coin.DOGE -> "Dogecoin"
        Coin.DGB -> "Digibyte"
        Coin.SOL -> "Solana"
        Coin.ADA -> "Cardano"
        Coin.DOT -> "Polkadot"
        Coin.MATIC -> "Polygon"
        Coin.ETC -> "Etherium Classic"
        Coin.BCH -> "Bitcoin Cash"
    }
}

fun Double.format(digits: Int = 8): String {
    val leading = toString().substringBefore(".").length
    return "%.${max(digits - leading, 2)}f".format(this).trimEnd('0').trimEnd('.')
}
