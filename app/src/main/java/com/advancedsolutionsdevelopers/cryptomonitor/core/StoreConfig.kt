package com.advancedsolutionsdevelopers.cryptomonitor.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

public interface StoreConfig {
    public val sendIntentScope: CoroutineScope
    public val intentDispatcher: CoroutineDispatcher
}
