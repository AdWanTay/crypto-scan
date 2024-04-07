package com.advancedsolutionsdevelopers.cryptomonitor.domain.cache

import android.content.SharedPreferences
import com.advancedsolutionsdevelopers.cryptomonitor.core.ICache
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.ApplicationScope
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Currency
import com.advancedsolutionsdevelopers.cryptomonitor.domain.models.SettingsData
import javax.inject.Inject

@ApplicationScope
class SettingsCache @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ICache<SettingsData> {
    override fun put(data: SettingsData) {
        sharedPreferences.edit()
            .putBoolean(SP_KEY_IS_FIRST, false)
            .putBoolean(SP_KEY_NOTIFICATIONS_ENABLED, data.notificationsEnabled)
            .putString(SP_KEY_CURRENCY, data.currency.name).apply()
    }

    override fun get(): SettingsData {
        return with(sharedPreferences) {
            if (contains(SP_KEY_IS_FIRST)) {
                SettingsData(
                    isFirstLaunch = getBoolean(SP_KEY_IS_FIRST, false),
                    notificationsEnabled = getBoolean(SP_KEY_NOTIFICATIONS_ENABLED, false),
                    currency = Currency.valueOf(getString(SP_KEY_CURRENCY, "") ?: "")
                )
            } else SettingsData()
        }
    }

    override fun drop() {
        sharedPreferences.edit().remove(SP_KEY_CURRENCY).remove(SP_KEY_IS_FIRST).remove(SP_KEY_NOTIFICATIONS_ENABLED).apply()
    }

    private companion object {
        const val SP_KEY_IS_FIRST = "first"
        const val SP_KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        const val SP_KEY_CURRENCY = "currency"
    }
}