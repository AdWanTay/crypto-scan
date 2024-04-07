package com.advancedsolutionsdevelopers.cryptomonitor.core.usecase

import kotlinx.coroutines.flow.Flow

public interface IFlowUseCase<TParams, TResult> {
    public fun execute(params: TParams): Flow<TResult>
}
