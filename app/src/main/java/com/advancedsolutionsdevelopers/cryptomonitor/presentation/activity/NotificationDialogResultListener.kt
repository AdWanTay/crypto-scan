package com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity

import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin

interface NotificationDialogResultListener {
    fun onResult(dialogResult: DialogResult)
}

sealed interface DialogResult{
    data class Delete(val coin: Coin): DialogResult
    data class Save(val coin: Coin, val price: Double): DialogResult
}