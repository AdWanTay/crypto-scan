package com.advancedsolutionsdevelopers.cryptomonitor.presentation.coinslist

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment
import com.advancedsolutionsdevelopers.cryptomonitor.CONST
import com.advancedsolutionsdevelopers.cryptomonitor.CONST.COIN_TYPE_ARG
import com.advancedsolutionsdevelopers.cryptomonitor.R
import com.advancedsolutionsdevelopers.cryptomonitor.core.viewBinding
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Coin
import com.advancedsolutionsdevelopers.cryptomonitor.data.models.Currency
import com.advancedsolutionsdevelopers.cryptomonitor.databinding.NotificationDialogBinding

import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.DialogResult
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.activity.NotificationDialogResultListener
import com.advancedsolutionsdevelopers.cryptomonitor.presentation.format


class NotificationDialog : DialogFragment(R.layout.notification_dialog) {
    private val binding by lazy { NotificationDialogBinding.inflate(layoutInflater) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val price = arguments?.getDouble(CONST.COIN_PRICE_ARG, 0.0) ?: 0.0
        val currency = Currency.valueOf(
            arguments?.getString(CONST.CURRENCY_ARG, Currency.USD.name) ?: Currency.USD.name
        )
        binding.currencyTextView.text = currency.symbol
        binding.targetPriceEditText.setText(price.format())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val coin = Coin.valueOf(arguments?.getString(COIN_TYPE_ARG, Coin.BTC.name) ?: Coin.BTC.name)
        return activity?.let {
            val builder = AlertDialog.Builder(ContextThemeWrapper(it, R.style.AlertDialogCustom))
            builder.setTitle(R.string.text_target_price).setView(binding.root)
                .setPositiveButton(R.string.text_save) { _, _ ->
                    (requireActivity() as NotificationDialogResultListener).onResult(
                        DialogResult.Save(
                            coin = coin,
                            price = binding.targetPriceEditText.text.toString().replace(',', '.')
                                .toDouble()
                        )
                    )
                }
                .setNeutralButton(R.string.text_delete) { _, _ ->
                    (requireActivity() as NotificationDialogResultListener).onResult(
                        DialogResult.Delete(
                            coin
                        )
                    )
                }
                .setNegativeButton(R.string.text_cancel) { _, _ ->
                    dialog?.cancel()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}