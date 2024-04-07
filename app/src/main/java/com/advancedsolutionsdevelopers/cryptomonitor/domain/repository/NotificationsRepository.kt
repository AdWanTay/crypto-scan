package com.advancedsolutionsdevelopers.cryptomonitor.domain.repository

import com.advancedsolutionsdevelopers.cryptomonitor.core.BaseStateFlowRepository
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.ApplicationScope
import com.advancedsolutionsdevelopers.cryptomonitor.domain.cache.NotificationsCache
import com.advancedsolutionsdevelopers.cryptomonitor.domain.models.NotificationsData
import javax.inject.Inject

@ApplicationScope
class NotificationsRepository @Inject constructor(
    private val notificationsCache: NotificationsCache
) : BaseStateFlowRepository<NotificationsData>(notificationsCache.get()) {
    override fun reduce(reducer: (state: NotificationsData) -> NotificationsData) {
        super.reduce(reducer)
        notificationsCache.reduce(reducer)
    }
}