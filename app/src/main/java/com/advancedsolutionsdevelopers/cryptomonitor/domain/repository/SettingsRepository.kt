package com.advancedsolutionsdevelopers.cryptomonitor.domain.repository

import com.advancedsolutionsdevelopers.cryptomonitor.core.BaseStateFlowRepository
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.ApplicationScope
import com.advancedsolutionsdevelopers.cryptomonitor.domain.cache.SettingsCache
import com.advancedsolutionsdevelopers.cryptomonitor.domain.models.SettingsData
import javax.inject.Inject

@ApplicationScope
class SettingsRepository @Inject constructor(
    private val settingsCache: SettingsCache
) : BaseStateFlowRepository<SettingsData>(settingsCache.get()) {
    override fun reduce(reducer: (state: SettingsData) -> SettingsData) {
        super.reduce(reducer)
        settingsCache.reduce(reducer)
    }
}