package com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines

import kotlinx.coroutines.CoroutineScope

public interface CoroutineScopes {

    public val appScope: CoroutineScope

    public val mainScope: CoroutineScope
}
