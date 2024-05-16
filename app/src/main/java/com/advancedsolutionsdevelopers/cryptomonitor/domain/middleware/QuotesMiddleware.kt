package com.advancedsolutionsdevelopers.cryptomonitor.domain.middleware

import com.advancedsolutionsdevelopers.cryptomonitor.core.Middleware
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineDispatchers
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.ActivityScope
import com.advancedsolutionsdevelopers.cryptomonitor.domain.repository.QuotesRepository
import com.advancedsolutionsdevelopers.cryptomonitor.domain.usecase.UpdateQuotesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScope
class QuotesMiddleware @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val updateQuotesUseCase: UpdateQuotesUseCase,
    private val quotesRepository: QuotesRepository
) : Middleware {
    override fun bindOnScope(scope: CoroutineScope): Job {
        return scope.launch(coroutineDispatchers.io) {
            updateQuotesUseCase.execute(Unit).collect { newQuotes ->
                quotesRepository.reduce {
                    newQuotes
                }
            }
        }
    }
}