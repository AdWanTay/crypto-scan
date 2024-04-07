package com.advancedsolutionsdevelopers.cryptomonitor.domain.cache

import android.content.SharedPreferences
import com.advancedsolutionsdevelopers.cryptomonitor.core.ICache
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.ApplicationScope
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.CoinItem
import com.advancedsolutionsdevelopers.cryptomonitor.domain.models.NotificationsData
import javax.inject.Inject

@ApplicationScope
class NotificationsCache @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ICache<NotificationsData> {
    override fun put(data: NotificationsData) {
        if (data.notifications.isNotEmpty()) {
            sharedPreferences.edit().putString(NOTIFICATIONS_KEY, data.toString()).apply()
        }
    }

    override fun get(): NotificationsData {
        return sharedPreferences.getString(NOTIFICATIONS_KEY, null).toNotificationsData()
    }

    override fun drop() {
        sharedPreferences.edit().remove(NOTIFICATIONS_KEY).apply()
    }

    public fun removeNotification(toRemove: CoinItem) {
        reduce { state: NotificationsData ->
            state.copy(
                notifications = state.notifications.filterKeys { coinKey -> coinKey != toRemove.type }
            )
        }
    }

    private fun String?.toNotificationsData(): NotificationsData {
        return if (this == null) {
            NotificationsData()
        } else {
            val notificationsMap = mutableMapOf<Coin, Double>()
            for (entry in split(',')) {
                notificationsMap[Coin.valueOf(entry.substringBefore('-'))] = entry.substringAfter('-').toDouble()
            }
            NotificationsData(notifications = notificationsMap)
        }
    }

    private companion object {
        const val NOTIFICATIONS_KEY = "notifications"
    }
}