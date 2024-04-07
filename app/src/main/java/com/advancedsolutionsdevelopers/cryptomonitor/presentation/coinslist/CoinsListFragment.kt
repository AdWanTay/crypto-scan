package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist

import android.os.Bundle
import android.view.View
import com.advancedsolutionsdevelopers.cryptomonitor.core.BaseFragment
import com.advancedsolutionsdevelopers.cryptomonitor.core.lazyViewModel
import com.advancedsolutionsdevelopers.cryptomonitor.databinding.FragmentCoinsListBinding
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.MainActivity
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist.recyclerView.CoinsListAdapter


class CoinsListFragment : BaseFragment<CoinsListState, CoinsListEvent>() {
    override val binding by lazy {
        FragmentCoinsListBinding.inflate(layoutInflater)
    }
    override val viewModel by lazyViewModel {
        (requireActivity() as MainActivity).activityComponent.coinsListViewModelFactory.create()
    }
    private val adapter = CoinsListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendEvent(CoinsListEvent.ViewCreated)
        binding.coinsRv.adapter = adapter
        binding.settingsButton.setOnClickListener { sendEvent(CoinsListEvent.OnSettingsClick) }
    }

    override fun render(state: CoinsListState) {
        super.render(state)
        when (state) {
            is CoinsListState.Loading -> {}
            is CoinsListState.Data -> {
                adapter.submitList(state.coinsList)
            }
        }
    }
}