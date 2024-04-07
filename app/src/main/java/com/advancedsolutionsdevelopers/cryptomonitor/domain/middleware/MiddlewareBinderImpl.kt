package com.advancedsolutionsdevelopers.cryptomonitor.domain.middleware

import com.advancedsolutionsdevelopers.cryptomonitor.core.Middleware
import com.advancedsolutionsdevelopers.cryptomonitor.core.MiddlewareBinder
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineScopes
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.ActivityScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScope
internal class MiddlewareBinderImpl @Inject constructor(
    private val coroutineScopes: CoroutineScopes,
    private val middlewares: Set<@JvmSuppressWildcards Middleware>,
) : MiddlewareBinder {
    @Volatile
    private var bindJob: Job? = null

    override fun bind(): Job? {
        synchronized(this) {
            bindJob = coroutineScopes.appScope.launch {
                middlewares.forEach { middleware -> middleware.bindOnScope(this) }
            }
            return bindJob
        }
    }

    override fun unbind() {
        bindJob?.cancel()
    }
}
