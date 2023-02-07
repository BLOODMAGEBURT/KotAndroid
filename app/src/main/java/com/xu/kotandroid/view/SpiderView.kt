package com.xu.kotandroid.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

/**
 * @Author Xu
 * Date：2023/2/7 14:38
 * Description：雷达图，蜘蛛图
 */
class SpiderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f

    private val radarPaint = Paint()
    private val pointPaint = Paint()

    private val polygonPath = Path()
    private val linePath = Path()
    private val pointPath = Path()

    private val points = mutableListOf<Int>()

    companion object {
        const val MAX = 6f
        const val POLYGON_SIDES = 6
    }

    init {
        radarPaint.apply {
            style = Paint.Style.STROKE
            color = Color.GREEN
        }

        pointPaint.apply {
            style = Paint.Style.FILL
            color = Color.CYAN
            alpha = 66
        }

        repeat(POLYGON_SIDES) {
            points.add(Random.nextInt(1, MAX.toInt()))
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        radius = min(w, h) / 2 * 0.9f

        centerX = w / 2f
        centerY = h / 2f

        super.onSizeChanged(w, h, oldw, oldh)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.translate(centerX, centerY)

        drawPolygon(canvas)
        drawLine(canvas)
        drawPoint(canvas)

    }

    private fun drawPolygon(canvas: Canvas) {

        (1..POLYGON_SIDES).forEach { i ->

            polygonPath.reset()
            val r = radius / POLYGON_SIDES * i

            (0..POLYGON_SIDES).forEach {
                if (it == 0) {
                    polygonPath.moveTo(r, 0f)
                } else {
                    val radian = ((360f / POLYGON_SIDES) * it).toRadian()
                    polygonPath.lineTo(r * cos(radian), r * sin(radian))
                }
            }
            canvas.drawPath(polygonPath, radarPaint)
        }
    }

    private fun drawLine(canvas: Canvas) {
        (0 until POLYGON_SIDES).forEach {

            val radian = ((360f / POLYGON_SIDES) * it).toRadian()

            linePath.lineTo(radius * cos(radian), radius * sin(radian))
            canvas.drawPath(linePath, radarPaint)
            linePath.reset()
        }

    }

    private fun drawPoint(canvas: Canvas) {

        (0 until POLYGON_SIDES).forEach {

            val radian = ((360f / POLYGON_SIDES) * it).toRadian()

            val percent = points[it] / MAX

            val x = radius * cos(radian) * percent
            val y = radius * sin(radian) * percent

            if (it == 0) {
                pointPath.moveTo(x, 0f)
            } else {
                pointPath.lineTo(x, y)
            }

            canvas.drawCircle(x, y, 10f, pointPaint)
        }

        pointPath.close()
        canvas.drawPath(pointPath, pointPaint)
    }


}

/**
 * 角度 转 弧度
 */
fun Float.toRadian() = (this * Math.PI / 180f).toFloat()