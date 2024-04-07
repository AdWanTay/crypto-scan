package com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.advancedsolutionsdevelopers.cryptomonitor.CoinApp
import com.advancedsolutionsdevelopers.cryptomonitor.core.MiddlewareBinder
import com.advancedsolutionsdevelopers.cryptomonitor.core.lazyViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NotificationDialogResultListener {
    val activityComponent by lazy {
        (application as CoinApp).applicationComponent.activityComponentFactory().create()
    }
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val navController by lazy {
        binding.navHostFragment.findNavController()
    }
    private val viewModel by lazyViewModel {
        activityComponent.mainActivityViewModelFactory.create()
    }

    @Inject
    lateinit var middlewareBinder: MiddlewareBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        activityComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        middlewareBinder.bind()
        viewModel.bindNavController(navController)
        viewModel.bindFragmentManager(supportFragmentManager)
    }

    override fun onPause() {
        super.onPause()
        viewModel.unbindNavController()
        viewModel.unbindFragmentManager()
        middlewareBinder.unbind()
    }

    override fun onResult(dialogResult: DialogResult) {
        when (dialogResult) {
            is DialogResult.Delete -> viewModel.deleteNotification(dialogResult.coin)
            is DialogResult.Save -> viewModel.setNotification(dialogResult.coin, dialogResult.price)
        }
    }
}