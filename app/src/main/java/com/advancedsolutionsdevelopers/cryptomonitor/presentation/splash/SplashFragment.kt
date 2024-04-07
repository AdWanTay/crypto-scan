package com.advancedsolutionsdevelopers.cryptomonitor.presentation.splash

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.advancedsolutionsdevelopers.cryptomonitor.R
import com.advancedsolutionsdevelopers.cryptomonitor.core.BaseFragment
import com.advancedsolutionsdevelopers.cryptomonitor.core.lazyViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Currency
import com.advancedsolutionsdevelopers.cryptomonitor.databinding.FragmentSplashBinding
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.MainActivity

class SplashFragment : BaseFragment<SplashState, SplashEvent>() {
    override val binding by lazy { FragmentSplashBinding.inflate(layoutInflater) }
    private lateinit var arl: ActivityResultLauncher<String>
    override val viewModel by lazyViewModel {
        (requireActivity() as MainActivity).activityComponent.splashViewModelFactory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arl = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                notificationGranted()
            } else {
                notificationDenied()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        initNotificationPermissionButton()
        initNextButton()
    }

    private fun initNotificationPermissionButton() {
        binding.enableNotificationsButton.setOnClickListener {
            askForPerm()
        }
    }

    private fun notificationGranted() {
        sendEvent(SplashEvent.NotificationsGranted)
    }

    private fun notificationDenied() {
        sendEvent(SplashEvent.NotificationsDenied)
    }

    private fun askForPerm() {
        if (android.os.Build.VERSION.SDK_INT > 32) {
            arl.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            notificationGranted()
        }
    }

    private fun initNextButton() {
        binding.nextButton.setOnClickListener {
            sendEvent(SplashEvent.OnNextClick)
        }
    }

    private fun initSpinner() {
        val langAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, Currency.entries.toTypedArray())
        binding.currencySpinner.adapter = langAdapter
        binding.currencySpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sendEvent(SplashEvent.CurrencySelected(Currency.entries[position]))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    @SuppressLint("SetTextI18n")
    override fun render(state: SplashState) {
        super.render(state)
        val notificationTextAdding = when (state.notificationsButtonState) {
            NotificationsButtonState.INITIAL -> ""
            NotificationsButtonState.GRANTED -> "✅"
            NotificationsButtonState.DENIED -> "⛔️"
        }
        binding.enableNotificationsButton.text = "$notificationTextAdding ${getString(R.string.text_button_enable_notifications)} $notificationTextAdding"

        binding.enableNotificationsButton.isEnabled = state.notificationsButtonState == NotificationsButtonState.INITIAL
    }
}