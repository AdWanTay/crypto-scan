package com.advancedsolutionsdevelopers.cryptomonitor.di

import android.content.Context
import com.advancedsolutionsdevelopers.cryptomonitor.CoinApp
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.ApplicationScope
import com.advancedsolutionsdevelopers.cryptomonitor.di.module.AppModule
import com.advancedsolutionsdevelopers.cryptomonitor.di.module.NetworkModule
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [AppModule::class, NetworkModule::class]
)
interface ApplicationComponent {

    fun activityComponentFactory(): ActivityComponent.Factory

    fun inject(app: CoinApp)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            context: Context
        ): ApplicationComponent
    }
}