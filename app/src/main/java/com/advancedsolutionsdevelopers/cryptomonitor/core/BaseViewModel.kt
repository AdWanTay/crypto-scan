package com.advancedsolutionsdevelopers.cryptomonitor.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

abstract class BaseViewModel<STATE : Any, EVENT : Any>(
    private val storeConfig: StoreConfig,
    initialState: STATE
) : ViewModel() {

    private val scope = viewModelScope + storeConfig.intentDispatcher
    private val internalStateFlow = MutableStateFlow(initialState)
    private val internalEventsFlow = MutableSharedFlow<EVENT>(replay = 0, extraBufferCapacity = Int.MAX_VALUE)
    private val dispatchChannel = Channel<suspend StoreContext<STATE>.() -> Unit>(Channel.UNLIMITED)

    private val context: StoreContext<STATE> = StoreContext(
        reduce = { reducer -> internalStateFlow.update { state -> reducer(state) } }
    )

    init {
        scope.launch {
            for (intent in dispatchChannel) {
                viewModelScope.launch { intent.invoke(context) }
            }
        }
        scope.launch {
            internalEventsFlow.collect(::handleEvent)
        }
    }

    protected abstract fun handleEvent(event: EVENT)

    internal fun emitEvent(event: EVENT) {
        internalEventsFlow.tryEmit(event)
    }

    internal fun observe(
        lifecycleOwner: LifecycleOwner,
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        state: ((state: STATE) -> Unit)? = null,
    ) {
        with(lifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(lifecycleState) {
                    state?.let { launch { internalStateFlow.collect { state(it) } } }
                }
            }
        }
    }

    fun intent(intent: suspend StoreContext<STATE>.() -> Unit) {
        if (dispatchChannel.trySend(intent).isFailure) {
            storeConfig.sendIntentScope.launch {
                dispatchChannel.send(intent)
            }
        }
    }
}