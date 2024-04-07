package com.advancedsolutionsdevelopers.cryptomonitor.data.background

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject

//фабрика для фонового загрузчика
class BackgroundWorkerFactory @Inject constructor(
    private val updateDataWorker: BackgroundSyncWorker.Factory
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            BackgroundSyncWorker::class.java.name -> {
                updateDataWorker.create(appContext, workerParameters)
            }

            else -> null
        }
    }
}