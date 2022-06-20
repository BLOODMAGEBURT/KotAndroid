package com.xu.kotandroid.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.xu.kotandroid.R

/**
 * @Author Xu
 * Date：2022/6/20 16:01
 * Description：
 */
class PanView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private lateinit var paint: Paint


    val data = listOf(0.23f, 0.20f, 0.27f, 0.15f, 0.15f)
//    val data = listOf(0.23f, 0.20f)

    private val colors = listOf(
        R.color.mini_fen_shi_up,
        R.color.purple_200,
        R.color.purple_700,
        R.color.teal_200,
        R.color.teal_700,
    )

    init {
        initPaint()
    }


    private fun initPaint() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        canvas.translate(measuredWidth / 2f, measuredHeight / 2f)

        val r = (measuredWidth.coerceAtMost(measuredHeight) / 2 * 0.5f) // 饼状图半径

        val rect = RectF(-r, -r, r, r)

        canvas.save()
        data.forEachIndexed { index, angle ->
            paint.color = context.getColor(colors[index])
            canvas.drawArc(rect, -90f, angle * 360, false, paint)
            canvas.rotate(angle * 360)


        }
        canvas.restore()

        canvas.save()
        paint.strokeWidth = 2f
        data.forEachIndexed { index, angle ->
            paint.color = context.getColor(colors[index])

            canvas.rotate(angle * 360 / 2f)
            canvas.drawLine(0f, 0f, 0f, -r, paint)
            canvas.rotate(angle * 360 / 2f)
        }
        canvas.restore()
    }
}

data class Pie(val value: Float, val percent: Float)