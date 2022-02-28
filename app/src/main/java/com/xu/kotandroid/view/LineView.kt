package com.xu.kotandroid.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.xu.kotandroid.R
import com.xu.kotandroid.px

/**
 * @Author Xu
 * Date：2021/11/11 10:04 上午
 * Description：
 */
class LineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {


    private val circlePaint = Paint()


    init {
        circlePaint.apply {
            color = Color.argb(100, 188, 188, 30)
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        canvas.drawColor(Color.argb(100, 188, 188, 188))

        canvas.drawCircle(measuredWidth / 2f, measuredHeight / 2f, 50.px, circlePaint)
    }


}