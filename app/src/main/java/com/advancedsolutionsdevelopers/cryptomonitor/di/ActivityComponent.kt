package com.advancedsolutionsdevelopers.cryptomonitor.di

import com.advancedsolutionsdevelopers.cryptomonitor.core.di.ActivityScope
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.IComponentFactory
import com.advancedsolutionsdevelopers.cryptomonitor.di.module.MiddlewareModule
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.MainActivity
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.MainActivityViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.coindetails.CoinDetailsViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist.CoinsListViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.settings.SettingsViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.splash.SplashViewModel
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [MiddlewareModule::class])
interface ActivityComponent {

    val splashViewModelFactory: SplashViewModel.Factory

    val coinsListViewModelFactory: CoinsListViewModel.Factory

    val coinDetailsViewModelFactory: CoinDetailsViewModel.Factory

    val settingsViewModelFactory: SettingsViewModel.Factory

    val mainActivityViewModelFactory: MainActivityViewModel.Factory

    fun inject(activity: MainActivity)

    @Subcomponent.Factory
    interface Factory : IComponentFactory<ActivityComponent>
}