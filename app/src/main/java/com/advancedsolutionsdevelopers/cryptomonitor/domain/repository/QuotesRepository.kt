package com.advancedsolutionsdevelopers.cryptomonitor.domain.repository

import com.advancedsolutionsdevelopers.cryptomonitor.core.BaseStateFlowRepository
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.ApplicationScope
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.CoinItem
import javax.inject.Inject

@ApplicationScope
class QuotesRepository @Inject constructor() : BaseStateFlowRepository<List<CoinItem>>(listOf())