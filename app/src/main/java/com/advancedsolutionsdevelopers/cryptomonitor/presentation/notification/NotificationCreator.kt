package com.advancedsolutionsdevelopers.cryptomonitor.presentation.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.advancedsolutionsdevelopers.cryptomonitor.CONST.CHANNEL_ID
import com.advancedsolutionsdevelopers.cryptomonitor.CONST.CHANNEL_NAME
import com.advancedsolutionsdevelopers.cryptomonitor.CONST.COIN_TYPE_ARG
import com.advancedsolutionsdevelopers.cryptomonitor.R
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.CoinItem
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Currency
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinName
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.format
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.toIconRes

class NotificationCreator() {

    fun notify(context: Context, coinItem: CoinItem, currency: Currency) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(buildChannel())
        val notification = buildNotification(
            context,
            coinItem.type.toIconRes(),
            context.getString(R.string.text_price_reached),
            context.getString(R.string.text_current_price, coinItem.type.coinName(), coinItem.price.format(), currency.symbol),
            buildNavigationIntent(context, coinItem.type)
        )
        manager.notify(coinItem.type.hashCode(), notification)
    }

    private fun buildNavigationIntent(context: Context, coin: Coin): PendingIntent =
        NavDeepLinkBuilder(context).setGraph(R.navigation.nav_graph)
            .setDestination(R.id.coinDetailsFragment).setArguments(bundleOf(COIN_TYPE_ARG to coin.name))
            .createPendingIntent()

    private fun buildNotification(
        context: Context,
        icon: Int,
        title: String,
        text: String,
        contentIntent: PendingIntent
    ): Notification =
        Notification.Builder(context, CHANNEL_ID).setSmallIcon(icon).setContentTitle(title)
            .setContentText(text).setContentIntent(contentIntent).build()

    private fun buildChannel() = NotificationChannel(
        CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
    )
}
