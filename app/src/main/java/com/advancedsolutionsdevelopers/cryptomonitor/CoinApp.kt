package com.advancedsolutionsdevelopers.cryptomonitor

import android.annotation.SuppressLint
import android.app.Application
import androidx.work.BackoffPolicy
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.advancedsolutionsdevelopers.cryptomonitor.data.background.BackgroundSyncWorker
import com.advancedsolutionsdevelopers.cryptomonitor.data.background.BackgroundWorkerFactory
import com.advancedsolutionsdevelopers.cryptomonitor.di.ApplicationComponent
import com.advancedsolutionsdevelopers.cryptomonitor.di.DaggerApplicationComponent
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CoinApp : Application() {
    private var _applicationComponent: ApplicationComponent? = null
    val applicationComponent: ApplicationComponent
        get() = requireNotNull(_applicationComponent!!) {
            "AppComponent must not be null!"
        }

    @Inject
    lateinit var workerFactory: BackgroundWorkerFactory
    private val configurationWorker by lazy {
        Configuration.Builder().setWorkerFactory(workerFactory).build()
    }

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()
        _applicationComponent = DaggerApplicationComponent.factory().create(this)
        applicationComponent.inject(this)
        WorkManager.initialize(this, configurationWorker)
        addSyncWorker()
    }

    /*Запускаем переодическое обновление данных в фоне
    (раз в 15 минут, в случае ошибки - работа попытается перезапуститься через 15 минут)*/
    private fun addSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()
        val myWorkRequest = PeriodicWorkRequestBuilder<BackgroundSyncWorker>(
            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
            TimeUnit.MINUTES
        ).setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.LINEAR, backoffDelay, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                WORKER_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                myWorkRequest
            )
    }

    companion object {
        const val WORKER_NAME = "sync"
        const val backoffDelay = 15L
    }
}