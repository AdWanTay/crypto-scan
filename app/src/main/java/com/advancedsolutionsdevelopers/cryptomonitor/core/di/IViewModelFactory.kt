package com.advancedsolutionsdevelopers.cryptomonitor.core.di

interface IViewModelFactory<TViewModel> {
    public fun create(): TViewModel
}