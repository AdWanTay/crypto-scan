package com.advancedsolutionsdevelopers.cryptomonitor.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

public abstract class BaseStateFlowRepository<T>(initialState: T) {
    private val mutableStateFlow: MutableStateFlow<T> = MutableStateFlow(initialState)
    public val stateFlow: StateFlow<T> = mutableStateFlow.asStateFlow()

    public open fun reduce(reducer: (state: T) -> T) {
        mutableStateFlow.update(reducer)
    }
}