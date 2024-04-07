package com.advancedsolutionsdevelopers.cryptomonitor.core.usecase

public interface IUseCase<TParams, TResult> {
    public suspend fun execute(params: TParams): TResult
}