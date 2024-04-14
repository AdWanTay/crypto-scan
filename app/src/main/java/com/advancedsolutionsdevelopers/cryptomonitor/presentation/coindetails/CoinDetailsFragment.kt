package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coindetails

import android.os.Bundle
import android.view.View
import com.advancedsolutionsdevelopers.cryptomonitor.R
import com.advancedsolutionsdevelopers.cryptomonitor.core.BaseFragment
import com.advancedsolutionsdevelopers.cryptomonitor.core.lazyViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.core.viewBinding
import com.advancedsolutionsdevelopers.cryptomonitor.databinding.FragmentCoinDetailsBinding
import com.advancedsolutionsdevelopers.cryptomonitor.databinding.NotificationDialogBinding
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.MainActivity

class CoinDetailsFragment : BaseFragment<CoinDetailsState, CoinDetailsEvent>(R.layout.fragment_coin_details) {
    override val binding by viewBinding(FragmentCoinDetailsBinding::bind)
    override val viewModel by lazyViewModel {
        (requireActivity() as MainActivity).activityComponent.coinDetailsViewModelFactory.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener { sendEvent(CoinDetailsEvent.OnBackClick) }
    }
    override fun render(state: CoinDetailsState) {
        super.render(state)
    }
}