package com.advancedsolutionsdevelopers.cryptomonitor.di.module

import com.advancedsolutionsdevelopers.cryptomonitor.core.Middleware
import com.advancedsolutionsdevelopers.cryptomonitor.core.MiddlewareBinder
import com.advancedsolutionsdevelopers.cryptomonitor.domain.middleware.QuotesMiddleware
import com.advancedsolutionsdevelopers.cryptomonitor.domain.middleware.MiddlewareBinderImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
internal interface MiddlewareModule {

    @Binds
    fun middlewareBinder(impl: MiddlewareBinderImpl): MiddlewareBinder

    @Binds
    @IntoSet
    fun binanceQuotesMiddleware(impl: QuotesMiddleware): Middleware
}