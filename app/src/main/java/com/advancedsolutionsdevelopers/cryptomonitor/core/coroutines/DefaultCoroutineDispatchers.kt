package com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines

import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

public class DefaultCoroutineDispatchers @Inject constructor() : CoroutineDispatchers {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val mainImmediate: CoroutineDispatcher = Dispatchers.Main.immediate
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val io: CoroutineDispatcher = Dispatchers.IO
}
