package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coindetails

import android.os.Bundle
import android.view.View
import com.advancedsolutionsdevelopers.cryptomonitor.CONST
import com.advancedsolutionsdevelopers.cryptomonitor.R
import com.advancedsolutionsdevelopers.cryptomonitor.core.BaseFragment
import com.advancedsolutionsdevelopers.cryptomonitor.core.lazyViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.core.viewBinding
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import com.advancedsolutionsdevelopers.cryptomonitor.databinding.FragmentCoinDetailsBinding
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.MainActivity
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.format
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.toIconRes

class CoinDetailsFragment :
    BaseFragment<CoinDetailsState, CoinDetailsEvent>(R.layout.fragment_coin_details) {
    override val binding by viewBinding(FragmentCoinDetailsBinding::bind)
    override val viewModel by lazyViewModel {
        (requireActivity() as MainActivity).activityComponent.coinDetailsViewModelFactory.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener { sendEvent(CoinDetailsEvent.OnBackClick) }
        val coinName = arguments?.getString(CONST.COIN_TYPE_ARG)
        if (!coinName.isNullOrBlank()) {
            sendEvent(CoinDetailsEvent.OnArgsReceived(coinName))
        }
    }

    override fun render(state: CoinDetailsState) {
        super.render(state)
        when (state) {
            is CoinDetailsState.Data -> {
                with(binding){
                    tvCoinName.text = state.coinName
                    tvMaxPrice.text = " ${state.maxPrice.format()}$"
                    tvMinPrice.text = " ${state.minPrice.format()}$"
                    tvVolume.text = " ${state.volume.format()}$"
                    coinIconImage.setImageResource(Coin.valueOf(state.coinName).toIconRes())
                }
            }
            is CoinDetailsState.Initial -> Unit
        }
    }
}