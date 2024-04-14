package com.advancedsolutionsdevelopers.cryptomonitor.core

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private class ViewBindingDelegate<in T : Any, out TViewBinding : ViewBinding>(
    private val lifecycleSupplier: () -> LifecycleOwner,
    private val viewBindingSupplier: () -> TViewBinding
) : ReadOnlyProperty<T, TViewBinding> {
    private var viewBinding: TViewBinding? = null

    private val lifecycleObserver = object : DefaultLifecycleObserver {
        private val mainHandler = Handler(Looper.getMainLooper())

        override fun onDestroy(owner: LifecycleOwner) {
            owner.lifecycle.removeObserver(this)
            if (!mainHandler.post { resetViewBinding() }) {
                resetViewBinding()
            }
        }
    }

    private fun resetViewBinding() {
        checkIsMainThread()
        viewBinding = null
    }

    override fun getValue(thisRef: T, property: KProperty<*>): TViewBinding {
        checkIsMainThread()

        val binding = viewBinding
        return if (binding != null) {
            binding
        } else {
            val lifecycle = lifecycleSupplier().lifecycle
            if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
                throw IllegalStateException("Binding is unavailable, view already destroyed.")
            }
            lifecycle.addObserver(lifecycleObserver)
            viewBindingSupplier().also { this.viewBinding = it }
        }
    }

    private fun checkIsMainThread() = check(
        value = Looper.myLooper() == Looper.getMainLooper(),
        lazyMessage = { "Trying attempt access to binding in background thread " }
    )
}

private fun Fragment.lifecycleSupplier(): () -> LifecycleOwner = when (this) {
    is DialogFragment -> {
        if (view == null) {
            { this }
        } else {
            { viewLifecycleOwner }
        }
    }
    else -> {
        { viewLifecycleOwner }
    }
}

public fun <TViewBinding : ViewBinding> Fragment.viewBinding(
    viewBindingProducer: (View) -> TViewBinding
): ReadOnlyProperty<Fragment, TViewBinding> {
    val viewBindingSupplier = { viewBindingProducer(requireView()) }
    return ViewBindingDelegate(lifecycleSupplier(), viewBindingSupplier)
}