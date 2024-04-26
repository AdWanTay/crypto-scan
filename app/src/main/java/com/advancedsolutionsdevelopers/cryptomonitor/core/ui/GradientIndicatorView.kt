package com.advancedsolutionsdevelopers.cryptomonitor.core.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.advancedsolutionsdevelopers.cryptomonitor.R
import com.advancedsolutionsdevelopers.cryptomonitor.databinding.ViewGradientIndigcatorBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@SuppressLint("CustomViewStyleable")
class GradientIndicatorView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val _state = MutableStateFlow(GradientIndicatorState.getDefault())
    public val state = _state.value
    public val buttonScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    public val binding = ViewGradientIndigcatorBinding.inflate(LayoutInflater.from(context), this)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.GradientIndicator,
            defStyleAttr,
            0
        )
        try {
            val minVal = typedArray.getString(R.styleable.GradientIndicator_minVal)
            val maxVal = typedArray.getString(R.styleable.GradientIndicator_maxVal)
            val currentVal = typedArray.getString(R.styleable.GradientIndicator_currentVal)
            val leftColor =
                typedArray.getColor(R.styleable.GradientIndicator_leftColor, Color.BLACK)
            val rightColor =
                typedArray.getColor(R.styleable.GradientIndicator_leftColor, Color.WHITE)
            init(
                minVal = minVal?.toDouble() ?: 0.0,
                maxVal = maxVal?.toDouble() ?: 0.0,
                currentVal = currentVal?.toDouble() ?: 0.0,
                leftColor = leftColor,
                rightColor = rightColor
            )

        } finally {
            typedArray.recycle()
        }
    }

    override fun onDetachedFromWindow() {
        buttonScope.cancel()
        super.onDetachedFromWindow()
    }

    init {
        buttonScope.launch {
            _state.collect(::render)
        }
    }

    /**
     * Init indicator.
     * */
    fun init(
        minVal: Double,
        maxVal: Double,
        currentVal: Double,
        leftColor: Int,
        rightColor: Int
    ) {
        _state.update { oldState ->
            oldState.copy(
                minVal = minVal,
                maxVal = maxVal,
                currentVal = currentVal,
                leftColor = leftColor,
                rightColor = rightColor
            )
        }
    }

    /**
     * Set new current value for indicator.
     * */
    fun setCurrent(currentVal: Double) {
        with(state) {
            if (currentVal < state.minVal) {
                init(currentVal, this.maxVal, currentVal, this.leftColor, this.rightColor)
            } else if (currentVal > state.maxVal) {
                init(this.minVal, currentVal, currentVal, this.leftColor, this.rightColor)
            } else {
                init(this.minVal, this.maxVal, currentVal, this.leftColor, this.rightColor)
            }
        }
    }

    private fun setDotPos(minVal: Double, maxVal: Double, currentVal: Double) {
        val pos = (currentVal - minVal) / (maxVal - minVal)
        binding.dot.x = (width * pos).toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        _state.update { oldState ->
            oldState.copy(width = w)
        }
    }

    private fun drawGradient(leftColor: Int, rightColor: Int) {
        val grad = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(leftColor, rightColor)
        )
        binding.lineBackground.setImageDrawable(grad)
    }

    private fun render(state: GradientIndicatorState) {
        setDotPos(state.minVal, state.maxVal, state.currentVal)
        drawGradient(state.leftColor, state.rightColor)
    }
}

data class GradientIndicatorState(
    val minVal: Double,
    val maxVal: Double,
    val currentVal: Double,
    val leftColor: Int,
    val rightColor: Int,
    val width: Int = 0
) {
    companion object {
        fun getDefault(): GradientIndicatorState {
            return GradientIndicatorState(
                0.0,
                1.0,
                0.5,
                Color.BLACK,
                Color.WHITE,
                0
            )
        }
    }
}