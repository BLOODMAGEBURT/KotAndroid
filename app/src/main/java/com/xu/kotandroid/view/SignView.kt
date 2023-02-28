package com.xu.kotandroid.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.xu.kotandroid.pt

/**
 * @Author Xu
 * Date：2023/2/28 17:49
 * Description：签名View
 */
class SignView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attributeSet, defStyleAttr) {


    private val path = Path()
    private val paint = Paint()

    init {
        initPaint()
    }

    private fun initPaint() {
        paint.apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 5.pt
            isAntiAlias = true
            pathEffect = CornerPathEffect(3f)
            strokeJoin = Paint.Join.ROUND
        }
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(event.x, event.y)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(event.x, event.y)
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                performClick()
            }

        }



        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        callOnClick()
        return super.performClick()
    }
}