package com.xu.kotandroid.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.xu.kotandroid.px
import kotlin.math.cos
import kotlin.math.sin


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
    private val linePath = Path()


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

        canvas.drawCircle(measuredWidth / 2f, measuredHeight / 2f, 100.px, circlePaint)

        drawPath(canvas)

    }

    private fun drawPath(canvas: Canvas) {

        canvas.save()
        canvas.translate(measuredWidth / 2f, measuredHeight / 2f)

        drawPolygon(canvas, 6)
//        drawPolygon(canvas, 8)


        canvas.restore()
    }

    private fun drawPolygon(canvas: Canvas, count: Int) {
        canvas.save()

//        canvas.rotate(30f)
        val r = 100.px

        /**
         *
         * Math 三角函数中，sin、cos、tan 函数参数使用的都是弧度
         * 1rad  = 180° / π
         * 1(弧度) = 180 / π (角度)
         * 1(角度) = π / 180 (弧度)
         * 弧度60，求角度 --> 60 * 180 / π
         * 角度45，求弧度 --> 45 * π / 180
         *
         */


        val angle = (360f / count) * (Math.PI / 180).toFloat()

        (0..count).forEach {
            if (it == 0) {
                linePath.moveTo(r, 0f)
            } else {
                linePath.lineTo(r * cos(angle * it), r * sin(angle * it))
            }
        }
        circlePaint.color = Color.RED
        canvas.drawPath(linePath, circlePaint)
        canvas.restore()

    }


}