package com.chizhikov.loadinganimation.loadingView

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.PathInterpolator
import com.chizhikov.loadinganimation.R
import kotlin.math.*

class OwnDrawLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var currentMatrix = 0

    //Defaults
    private var crossWidth = 6f
    private var crossLength = 22f

    private var dotWidth = 6f

    private var cornerRadius = 2.5f

    private var margin = 16f

    private var dotDiameter = dotWidth
    private var dotMargin = crossLength - 2 * crossWidth
    private var fullDotsWidth = dotDiameter * 2 + dotMargin
    private var dotsHypot = hypot(fullDotsWidth, fullDotsWidth)

    private var shapeColor = 0xFFE1E3E6.toInt()

    private var animationDelay = 1000L

    private var animationDuration = 300L

    private var scaleTo = 1.3f

    //Defaults in dp
    private var crossWidthDp = dp(crossWidth)
    private var crossLengthDp = dp(crossLength)

    private var dotWidthDp = dp(dotWidth)

    private var cornerRadiusDp = dp(cornerRadius)

    private val marginDp = dp(margin)

    private var dotMarginDp = dp(crossLength - 2 * crossWidth)

    private var dotDiameterDp = dp(dotDiameter)

    private var fullDotsWidthDp = dotDiameterDp * 2 + dotMarginDp

    private var dotsHypotDp = hypot(fullDotsWidthDp, fullDotsWidthDp)

    private val crossHypotDp = hypot(crossLengthDp, crossLengthDp)

    init {
        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.OwnDrawLoadingView, defStyleAttr, 0
        )
        try {
            shapeColor = a.getColor(R.styleable.OwnDrawLoadingView_color, shapeColor)
            margin = a.getFloat(R.styleable.OwnDrawLoadingView_crossDotMargin, margin)
            crossWidth = a.getFloat(R.styleable.OwnDrawLoadingView_crossWidth, crossWidth)
            crossWidthDp = dp(crossWidth)
            dotDiameterDp = crossWidthDp
            dotMarginDp = dp(crossLength - 2 * crossWidth)
            fullDotsWidthDp = dotDiameterDp * 2 + dotMarginDp
            dotsHypotDp = hypot(fullDotsWidthDp, fullDotsWidthDp)
            scaleTo = a.getFloat(R.styleable.OwnDrawLoadingView_scaleTo, scaleTo)
            cornerRadius = a.getFloat(R.styleable.OwnDrawLoadingView_cornerRadius, cornerRadius)
            animationDelay = a.getFloat(
                R.styleable.OwnDrawLoadingView_animationDelay,
                animationDelay.toFloat()
            ).toLong()
            animationDuration = a.getFloat(
                R.styleable.OwnDrawLoadingView_animationDuration,
                animationDuration.toFloat()
            ).toLong()
        } finally {
            a.recycle()
        }
    }

    //Paint
    private val crossPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = shapeColor
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = shapeColor
    }


    //Desired
    private val desiredWidth = (crossHypotDp + marginDp + dotsHypotDp)
    private val desiredHeight = max(crossHypotDp, dotsHypotDp)

    //Init parts of crossView
    private val verticalRectangle = RectF()
    private val horizontalRectangle = RectF()


    //Init parts of dots
    private val leftDot = RectF()
    private val topDot = RectF()
    private val rightDot = RectF()
    private val bottomDot = RectF()


    //Animation part
    private var animator: Animator? = null

    private var rectangleRotate: Float = 0F
        set(value) {
            field = value
            invalidate()
        }

    private val rectangleRotateAnimator = ValueAnimator.ofFloat(0.0F, 180F).apply {
        duration = animationDuration
        addUpdateListener {
            rectangleRotate = it.animatedValue as Float
        }
    }

    private var dotLeftScale: Float = 1f
        set(value) {
            field = value
            invalidate()
        }

    private var dotTopScale: Float = 1f
        set(value) {
            field = value
            invalidate()
        }

    private var dotRightScale: Float = 1f
        set(value) {
            field = value
            invalidate()
        }

    private var dotBottomScale: Float = 1f
        set(value) {
            field = value
            invalidate()
        }

    private val dotLeftAnimator = ValueAnimator
        .ofFloat(1f, 1f, 1f, 1f, 1f, scaleTo, 1f).apply {
            duration = 6 * animationDuration
            addUpdateListener {
                dotLeftScale = it.animatedValue as Float
            }
        }

    private val dotTopAnimator = ValueAnimator
        .ofFloat(1f, 1f, scaleTo, 1f, 1f, 1f).apply {
            duration = 5 * animationDuration
            addUpdateListener {
                dotTopScale = it.animatedValue as Float
            }
        }

    private val dotRightAnimator = ValueAnimator
        .ofFloat(1f, 1f, 1f, scaleTo, 1f, 1f).apply {
            duration = 5 * animationDuration
            addUpdateListener {
                dotRightScale = it.animatedValue as Float
            }
        }


    private val dotBottomAnimator = ValueAnimator
        .ofFloat(1f, 1f, 1f, 1f, scaleTo, 1f).apply {
            duration = 5 * animationDuration
            addUpdateListener {
                dotBottomScale = it.animatedValue as Float
            }
        }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        animator?.cancel()

        animator = AnimatorSet().apply {
            interpolator = PathInterpolator(0.25F, 0.1F, 0.25F, 1F)
            playTogether(
                rectangleRotateAnimator,
                dotLeftAnimator,
                dotTopAnimator,
                dotRightAnimator,
                dotBottomAnimator
            )
            startDelay = animationDelay

            //cyclic adapter
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    startDelay = animationDelay
                    start()
                }
            })
            start()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            getSize(widthMeasureSpec, desiredWidth.toInt()),
            getSize(heightMeasureSpec, desiredHeight.toInt())
        )
    }

    private fun onDrawCrossLine(
        canvas: Canvas,
        rectangle: RectF,
        cx: Float,
        cy: Float,
        cxShift: Float,
        cyShift: Float,
        rotate: Float,
        cornerRadius: Float
    ) {
        val save = canvas.save()

        canvas.rotate(rotate, cx, cy)
        rectangle.set(
            cx - cxShift / 2,
            cy - cyShift / 2,
            cx + cxShift / 2,
            cy + cyShift / 2
        )
        canvas.drawRoundRect(rectangle, cornerRadius, cornerRadius, crossPaint)

        canvas.restoreToCount(save)
    }


    private fun onDrawDot(
        canvas: Canvas,
        dot: RectF,
        cx: Float,
        cy: Float,
        scale: Float,
        cornerRadius: Float
    ) {
        val save = canvas.save()

        canvas.scale(scale, scale, cx, cy)
        dot.set(
            cx - dotDiameterDp / 2,
            cy - dotDiameterDp / 2,
            cx + dotDiameterDp / 2,
            cy + dotDiameterDp / 2
        )
        canvas.drawRoundRect(dot, cornerRadius, cornerRadius, dotPaint)

        canvas.restoreToCount(save)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val save = canvas.save()

        // center
        var cx = crossHypotDp / 2
        var cy = crossHypotDp / 2

        onDrawCrossLine(
            canvas,
            verticalRectangle,
            cx,
            cy,
            crossWidthDp,
            crossLengthDp,
            rectangleRotate,
            cornerRadiusDp
        )

        onDrawCrossLine(
            canvas,
            horizontalRectangle,
            cx,
            cy,
            crossLengthDp,
            crossWidthDp,
            rectangleRotate,
            cornerRadiusDp
        )

        cx = crossHypotDp + marginDp + dotsHypotDp / 2
        cy = crossHypotDp / 2


        onDrawDot(
            canvas,
            leftDot,
            cx - dotMarginDp / 2 - dotDiameterDp / 2,
            cy,
            dotLeftScale,
            cornerRadiusDp
        )

        onDrawDot(
            canvas,
            topDot,
            cx,
            cy - dotMarginDp / 2 - dotDiameterDp / 2,
            dotTopScale,
            cornerRadiusDp
        )

        onDrawDot(
            canvas,
            rightDot,
            cx + dotMarginDp / 2 + dotDiameterDp / 2,
            cy,
            dotRightScale,
            cornerRadiusDp
        )

        onDrawDot(
            canvas,
            bottomDot,
            cx,
            cy + dotMarginDp / 2 + dotDiameterDp / 2,
            dotBottomScale,
            cornerRadiusDp
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        animator?.cancel()
        animator = null
    }

    private fun getSize(measureSpec: Int, desired: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        return when (mode) {
            MeasureSpec.AT_MOST -> min(size, desired)
            MeasureSpec.EXACTLY -> size
            MeasureSpec.UNSPECIFIED -> desired
            else -> desired
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val state = CustomState(super.onSaveInstanceState())
        state.value = currentMatrix
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        state as CustomState
        super.onRestoreInstanceState(state.superState)
        currentMatrix = state.value
    }

    private class CustomState : BaseSavedState {
        var value: Int = 0

        constructor(superState: Parcelable?) : super(superState)
        constructor(parcel: Parcel) : super(parcel) {
            value = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(value)
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<CustomState> {
                override fun createFromParcel(source: Parcel): CustomState = CustomState(source)
                override fun newArray(size: Int): Array<CustomState?> = arrayOfNulls(size)
            }
        }
    }

    private fun dp(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }
}