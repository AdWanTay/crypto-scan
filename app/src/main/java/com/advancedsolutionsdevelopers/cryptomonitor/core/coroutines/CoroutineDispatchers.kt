package com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher

public interface CoroutineDispatchers {
    public val main: CoroutineDispatcher
    public val mainImmediate: CoroutineDispatcher
    public val default: CoroutineDispatcher
    public val io: CoroutineDispatcher
}
