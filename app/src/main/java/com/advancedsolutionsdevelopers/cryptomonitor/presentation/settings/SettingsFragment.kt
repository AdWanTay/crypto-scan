package com.advancedsolutionsdevelopers.cryptomonitor.presentation.settings

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.advancedsolutionsdevelopers.cryptomonitor.R
import com.advancedsolutionsdevelopers.cryptomonitor.core.BaseFragment
import com.advancedsolutionsdevelopers.cryptomonitor.core.lazyViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.core.viewBinding
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Currency
import com.advancedsolutionsdevelopers.cryptomonitor.databinding.FragmentCoinsListBinding
import com.advancedsolutionsdevelopers.cryptomonitor.databinding.FragmentSettingsBinding
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.MainActivity

class SettingsFragment : BaseFragment<SettingsState, SettingsEvent>(R.layout.fragment_settings) {
    override val binding by viewBinding(FragmentSettingsBinding::bind)
    override val viewModel by lazyViewModel {
        (requireActivity() as MainActivity).activityComponent.settingsViewModelFactory.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        binding.dropSettingsButton.setOnClickListener { sendEvent(SettingsEvent.OnResetClick) }
        binding.backButton.setOnClickListener { sendEvent(SettingsEvent.OnBackClick) }
    }

    private fun initSpinner() {
        val langAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, Currency.entries.toTypedArray())
        binding.currencySpinner.adapter = langAdapter
        binding.currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sendEvent(SettingsEvent.CurrencySelected(Currency.entries[position]))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun render(state: SettingsState) {
        super.render(state)
        binding.currencySpinner.setSelection(Currency.entries.indexOf(state.selectedCurrency))
    }
}