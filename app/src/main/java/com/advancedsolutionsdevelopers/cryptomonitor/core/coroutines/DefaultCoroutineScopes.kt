package com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines

import android.util.Log
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineDispatchers
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineScopes
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

public class DefaultCoroutineScopes @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
) : CoroutineScopes {

    override val appScope: CoroutineScope
        get() = CoroutineScope(
            context = dispatchers.default + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
                Log.e("appScope", "exception: $throwable")
            }
        )

    override val mainScope: CoroutineScope
        get() = CoroutineScope(context = dispatchers.mainImmediate + SupervisorJob())
}
