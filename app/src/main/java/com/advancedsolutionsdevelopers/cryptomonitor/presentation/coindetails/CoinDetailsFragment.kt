package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coindetails

import android.os.Bundle
import android.view.View
import com.advancedsolutionsdevelopers.cryptomonitor.core.BaseFragment
import com.advancedsolutionsdevelopers.cryptomonitor.core.lazyViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.databinding.FragmentCoinDetailsBinding
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.MainActivity

class CoinDetailsFragment : BaseFragment<CoinDetailsState, CoinDetailsEvent>() {
    override val binding by lazy {
        FragmentCoinDetailsBinding.inflate(layoutInflater)
    }
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