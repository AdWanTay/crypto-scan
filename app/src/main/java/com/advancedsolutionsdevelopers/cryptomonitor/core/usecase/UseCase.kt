package com.advancedsolutionsdevelopers.cryptomonitor.core.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

public abstract class UseCase<TParams, TResult>(private val dispatcher: CoroutineDispatcher) : IUseCase<TParams, TResult> {

    public override suspend fun execute(params: TParams): TResult = withContext(dispatcher) {
        run(params)
    }

    protected abstract suspend fun run(params: TParams): TResult
}

public suspend inline fun <T> IUseCase<Unit, T>.execute(): T = execute(Unit)
