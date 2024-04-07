package com.advancedsolutionsdevelopers.cryptomonitor.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<STATE : Any, EVENT : Any> : Fragment() {
    protected abstract val viewModel: BaseViewModel<STATE, EVENT>
    abstract val binding: ViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    protected open fun render(state: STATE): Unit = Unit

    fun sendEvent(event: EVENT) {
        viewModel.emitEvent(event)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(viewLifecycleOwner, state = ::render)
    }
}