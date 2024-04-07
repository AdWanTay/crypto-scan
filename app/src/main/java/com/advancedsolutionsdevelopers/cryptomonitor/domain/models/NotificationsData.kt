package com.advancedsolutionsdevelopers.cryptomonitor.domain.models

import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin

public data class NotificationsData(
    val notifications: Map<Coin, Double> = mapOf()
) {
    override fun toString(): String {
        if (notifications.isEmpty()) return ""
        return notifications.map { notification -> "${notification.key.name}-${notification.value}" }.reduce { acc, s -> "$acc,$s" }
    }
}