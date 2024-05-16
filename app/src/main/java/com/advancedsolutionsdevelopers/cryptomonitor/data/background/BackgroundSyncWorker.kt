package com.advancedsolutionsdevelopers.cryptomonitor.data.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineDispatchers
import com.advancedsolutionsdevelopers.cryptomonitor.domain.cache.NotificationsCache
import com.advancedsolutionsdevelopers.cryptomonitor.domain.cache.SettingsCache
import com.advancedsolutionsdevelopers.cryptomonitor.domain.usecase.UpdateQuotesUseCase
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.notification.NotificationCreator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class BackgroundSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val notificationsCache: NotificationsCache,
    private val settingsCache: SettingsCache,
    private val updateQuotesUseCase: UpdateQuotesUseCase,
    private val dispatchers: CoroutineDispatchers
) : CoroutineWorker(context, params) {
    private val notificationCreator = NotificationCreator()

    override suspend fun doWork(): Result {
        try {
            withContext(dispatchers.io) {
                val settings = settingsCache.get()
                if (settings.notificationsEnabled) {
                    val notifications = notificationsCache.get().notifications
                    val quotes = updateQuotesUseCase.execute(Unit).first()
                    for (i in quotes) {
                        if (i.type in notifications.keys && notifications.getValue(i.type) <= i.price) {
                            notificationCreator.notify(context, i, settings.currency)
                            notificationsCache.removeNotification(i)
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            return Result.retry()
        }
        return Result.success()
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, workerParameters: WorkerParameters): BackgroundSyncWorker
    }
}