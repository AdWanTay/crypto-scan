package com.advancedsolutionsdevelopers.cryptomonitor.core.usecase

import android.util.Log
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineDispatchers

public abstract class BackendUseCase<TParams, TRequest, TResponse, TResult>(
    dispatchers: CoroutineDispatchers,
) : UseCase<TParams, Result<TResult>>(dispatchers.io) {

    public override suspend fun run(params: TParams): Result<TResult> = try {
        performRequest(transformRequest(params))
            .map(::transformResponse)
    } catch (e: Exception) {
        Result.failure(e)
    }

    protected abstract fun transformRequest(params: TParams): TRequest
    protected abstract suspend fun performRequest(request: TRequest): Result<TResponse>
    protected abstract fun transformResponse(response: TResponse): TResult
}
