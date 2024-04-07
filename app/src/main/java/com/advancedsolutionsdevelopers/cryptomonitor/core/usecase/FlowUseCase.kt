package com.advancedsolutionsdevelopers.cryptomonitor.core.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

public abstract class FlowUseCase<TParams, TResult>(private val dispatcher: CoroutineDispatcher) : IFlowUseCase<TParams, TResult> {

    public override fun execute(params: TParams): Flow<TResult> = run(params).flowOn(dispatcher)

    protected abstract fun run(params: TParams): Flow<TResult>
}

public fun <T> IFlowUseCase<Unit, T>.execute(): Flow<T> = execute(Unit)
