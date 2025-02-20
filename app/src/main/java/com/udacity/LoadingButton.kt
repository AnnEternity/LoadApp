package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0


    private val valueAnimator = ValueAnimator.ofFloat(0f, 100f).apply {
        duration = 1000
        addUpdateListener {
            invalidate()
        }

    }

    private var startColor = 0
    private var downloadColor = 0
    private var circleColor = 0

    private var downloadString: CharSequence = ""
    private var loadingString: CharSequence = ""

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            startColor =
                getColor(R.styleable.LoadingButton_color1, context.getColor(R.color.colorPrimary))
            downloadColor = getColor(
                R.styleable.LoadingButton_color2,
                context.getColor(R.color.colorPrimaryDark)
            )
            circleColor = getColor(
                R.styleable.LoadingButton_colorCircle,
                context.getColor(R.color.colorAccent)
            )

            downloadString = getText(R.styleable.LoadingButton_downloadText) ?: "Download"
            loadingString = getText(R.styleable.LoadingButton_loadingText) ?: "We are Loading"
        }
    }

    override fun performClick(): Boolean {
        valueAnimator.start()
        if (super.performClick()) return true
        return true
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
       //start
        val progress = valueAnimator.animatedValue as Float
        paint.color = startColor
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        //progress
        paint.color = downloadColor
        canvas.drawRect(0f, 0f, widthSize.toFloat() * (progress/100), heightSize.toFloat(), paint)
        //text
        if (progress == 100f){
            paint.color = startColor
            canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        }
        paint.color = Color.WHITE
        if (progress == 0f || progress >= 100f) {
            canvas.drawText(
                downloadString, 0, downloadString.length,
                widthSize / 2f,
                (heightSize + (paint.textSize / 3 * 2)) / 2f, paint
            )
        } else canvas.drawText(
            loadingString, 0, loadingString.length,
            widthSize / 2f,
            (heightSize + (paint.textSize / 3 * 2)) / 2f, paint
        )
        //circle
        if (progress == 0f) {
            paint.color = circleColor
            var circleX =
                (widthSize / 2f) + (paint.textSize.toFloat() * loadingString.length / 4f) + paint.textSize
            canvas.drawCircle(
                circleX, heightSize.toFloat() / 2, paint.textSize / 2 * 0, paint
            )
        }
        if (progress > 0f && progress < 100f) {
            paint.color = circleColor
            var circleX =
                (widthSize / 2f) + (paint.textSize.toFloat() * loadingString.length / 4f) + paint.textSize
            val circleY = heightSize.toFloat() / 2
            val radius = paint.textSize / 2

            val left = (widthSize / 2f) + (paint.textSize.toFloat() * loadingString.length / 4f)
            val right =
                (widthSize / 2f) + (paint.textSize.toFloat() * loadingString.length / 4f) + paint.textSize
            val top = heightSize.toFloat() / 2 - paint.textSize /2
            val bottom = heightSize.toFloat() / 2 + paint.textSize /2

            val oval = RectF(left, top, right, bottom)

            canvas.drawArc(oval, 0f, progress * 3.6f , true, paint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}