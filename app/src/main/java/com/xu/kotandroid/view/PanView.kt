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
    private lateinit var linePaint: Paint
    private lateinit var textPaint: Paint


    val data = listOf(0.15f, 0.02f, 0.20f, 0.23f, 0.10f, 0.05f, 0.15f, 0.10f)
//    val data = listOf(0.23f, 0.20f)

    private val colors = listOf(
        R.color.mini_fen_shi_up,
        R.color.purple_200,
        R.color.purple_500,
        R.color.purple_700,
        R.color.teal_200,
        R.color.teal_700,
        R.color.mini_fen_shi_down,
        R.color.black
    )

    init {
        initPaint()
    }


    private fun initPaint() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f

        linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 2f


        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.color = Color.BLACK
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 12f

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        canvas.translate(measuredWidth / 2f, measuredHeight / 2f)

        // 饼状图半径
        val r = (measuredWidth.coerceAtMost(measuredHeight) / 2 * 0.5f)

        // 字


        // 画环
        drawRing(r, canvas)

        // 画线
        drawLine(r, canvas)

    }

    // 环
    private fun drawRing(r: Float, canvas: Canvas) {
        val rect = RectF(-r, -r, r, r)

        canvas.save()


        data.forEachIndexed { index, angle ->

            if (angle == 0f) {
                return@forEachIndexed
            }

            paint.color = context.getColor(colors[index])
            canvas.drawArc(rect, -90f, angle * 360, false, paint)
            canvas.rotate(angle * 360)
        }
        canvas.restore()

    }

    // 线,占比
    private fun drawLine(r: Float, canvas: Canvas) {
        canvas.save()
        var totalAngel = 0f
        data.forEachIndexed { index, angle ->

            if (angle == 0f) {
                return@forEachIndexed
            }
            linePaint.color = context.getColor(colors[index])



            canvas.rotate(angle * 360 / 2f)
            totalAngel += angle * 360 / 2f
            canvas.drawLine(0f, -r, 0f, -r - 30, linePaint)


            canvas.save()


            canvas.translate(0f, -r - 30)
            canvas.rotate(90 - totalAngel + if (index >= 4) 180f else 0f)
            canvas.drawLine(0f, 0f, 0f, -r / 2, linePaint)
            canvas.save()
            canvas.translate(0f, -r / 2)
            canvas.rotate(-90f)
            canvas.drawText("$angle%", 0f, 0f, textPaint)
            canvas.restore()



            canvas.restore()


            canvas.rotate(angle * 360 / 2f)
            totalAngel += angle * 360 / 2f
        }
        canvas.restore()
    }


}

data class Pie(val value: Float, val percent: Float)