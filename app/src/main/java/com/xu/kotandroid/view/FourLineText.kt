package com.xu.kotandroid.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.xu.kotandroid.pt
import kotlin.math.min

/**
 * @Author Xu
 * Date：2023/2/28 14:14
 * Description：带有四条线的textView
 */
class FourLineText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attributeSet, defStyleAttr) {

    private val paint = Paint()
    private val textPaint = Paint()
    private var xCenter = 0f
    private var yCenter = 0f
    private var radius = 0f


    init {
        initPaint()
    }

    private fun initPaint() {
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 1f
            isAntiAlias = true
        }

        textPaint.apply {
            style = Paint.Style.FILL
            textSize = 16.pt
            isAntiAlias = true
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        xCenter = w / 2f
        yCenter = h / 2f
        radius = min(xCenter, yCenter) / 2
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textPaint.color = Color.BLACK
        canvas.translate(xCenter, yCenter)

        val textWidth = textPaint.measureText("Hello World")
        val textHeight = textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent

        val dy = (textHeight / 2) - textPaint.fontMetrics.descent
        val textStartX = -textWidth / 2

        canvas.drawText("Hello World", textStartX, dy, textPaint)

        paint.color = Color.BLUE
        canvas.drawCircle(0f, 0f, radius, paint)
        canvas.drawPoint(0f, 0f, paint)


        textPaint.fontMetrics.apply {
            paint.color = Color.RED
            canvas.drawLine(textStartX, top + dy, textWidth / 2, top + dy, paint)

            paint.color = Color.YELLOW
            canvas.drawLine(textStartX, ascent + dy, textWidth / 2, ascent + dy, paint)

            paint.color = Color.GREEN
            canvas.drawLine(textStartX, descent + dy, textWidth / 2, descent + dy, paint)

            paint.color = Color.MAGENTA
            canvas.drawLine(textStartX, bottom + dy, textWidth / 2, bottom + dy, paint)
        }
    }

}