package com.advancedsolutionsdevelopers.cryptomonitor.core

import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineDispatchers
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineScopes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

public class DefaultStoreConfig @Inject constructor(
    coroutineScopes: CoroutineScopes,
    coroutineDispatchers: CoroutineDispatchers,
) : StoreConfig {
    override val sendIntentScope: CoroutineScope = coroutineScopes.appScope
    override val intentDispatcher: CoroutineDispatcher = coroutineDispatchers.default
}
