package com.advancedsolutionsdevelopers.cryptomonitor.di.module

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.preference.PreferenceManager
import com.advancedsolutionsdevelopers.cryptomonitor.core.DefaultStoreConfig
import com.advancedsolutionsdevelopers.cryptomonitor.core.StoreConfig
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineDispatchers
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.CoroutineScopes
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.DefaultCoroutineDispatchers
import com.advancedsolutionsdevelopers.cryptomonitor.core.coroutines.DefaultCoroutineScopes
import com.advancedsolutionsdevelopers.cryptomonitor.core.di.ApplicationScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface AppModule {
    companion object {
        @Provides
        @ApplicationScope
        fun provideSharedPreferences(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        @Provides
        @ApplicationScope
        fun provideConnectivityManager(context: Context): ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @ApplicationScope
    @Binds
    fun coroutineScopes(impl: DefaultCoroutineScopes): CoroutineScopes

    @ApplicationScope
    @Binds
    fun coroutineDispatchers(impl: DefaultCoroutineDispatchers): CoroutineDispatchers

    @ApplicationScope
    @Binds
    fun storeConfig(impl: DefaultStoreConfig): StoreConfig
}