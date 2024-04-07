package com.advancedsolutionsdevelopers.cryptomonitor.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

public interface Middleware {
    public fun bindOnScope(scope: CoroutineScope): Job
}