package com.advancedsolutionsdevelopers.cryptomonitor.domain.middleware

import com.advancedsolutionsdevelopers.cryptomonitor.CONST.REQUEST_DELAY
import com.advancedsolutionsdevelopers.cryptomonitor.core.Middleware
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineDispatchers
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.ActivityScope
import com.advancedsolutionsdevelopers.cryptomonitor.domain.usecase.UpdateQuotesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScope
class QuotesMiddleware @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val updateQuotesUseCase: UpdateQuotesUseCase
) : Middleware {
    override fun bindOnScope(scope: CoroutineScope): Job {
        return scope.launch(coroutineDispatchers.io) {
            while (true) {
                delay(REQUEST_DELAY)
                updateQuotesUseCase.execute(Unit)
            }
        }
    }
}