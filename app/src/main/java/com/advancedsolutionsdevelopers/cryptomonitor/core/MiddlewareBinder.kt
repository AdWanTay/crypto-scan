package com.advancedsolutionsdevelopers.cryptomonitor.core

import kotlinx.coroutines.Job

public interface MiddlewareBinder {

    public fun bind(): Job?

    public fun unbind()
}