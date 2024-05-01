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
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.getDescription
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.toIconRes
import com.google.common.io.Resources

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
                    val min = state.minPrice
                    val max = state.maxPrice
                    val current = state.currentPrice
                    indigcatorView.init(min, max, current, resources.getColor(R.color.base_green, null), resources.getColor(R.color.base_red, null))
                    tvCoinName.text = state.coinName
                    tvMaxPrice.text = " ${max.format()}$"
                    tvMinPrice.text = " ${min.format()}$"
                    tvVolume.text = " ${state.volume.format()}$"
                    tvCurrentPrice.text = " ${current.format()}$"
                    coinIconImage.setImageResource(Coin.valueOf(state.coinName).toIconRes())
                    tvDescription.text = getString(Coin.valueOf(state.coinName).getDescription())
                }
            }
            is CoinDetailsState.Initial -> Unit
        }
    }
}