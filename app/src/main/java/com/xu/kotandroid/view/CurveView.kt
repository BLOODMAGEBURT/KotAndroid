package com.xu.kotandroid.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.xu.kotandroid.bean.ViewPoint

/**
 * @Author Xu
 * Date：2021/11/9 2:12 下午
 * Description：
 */
class CurveView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    init {
        initPaint()
        initPath()
    }

    private fun initPath() {
        linePath = Path()
    }


    private lateinit var linePaint: Paint
    private lateinit var curvePaint: Paint
    private lateinit var pointPaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var linePath: Path


    private val pointList = listOf(
        ViewPoint(0f, 0f),
        ViewPoint(120f, 150f),
        ViewPoint(170f, 320f),
        ViewPoint(320f, 100f),
        ViewPoint(570f, 520f),
        ViewPoint(870f, 420f),
        ViewPoint(900f, 320f),

        )

    private val moneyList = listOf("0w", "150w", "320w", "100w", "520w", "420w", "320w")

    private fun initPaint() {
        linePaint = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.argb(100, 188, 188, 188)
            strokeWidth = 2f
        }

        curvePaint = Paint().apply {

            style = Paint.Style.STROKE
            color = Color.RED
            strokeWidth = 10f
        }

        pointPaint = Paint().apply {

            style = Paint.Style.FILL
            color = Color.CYAN
            strokeWidth = 15f
        }

        textPaint = Paint().apply {

            style = Paint.Style.FILL
            color = Color.MAGENTA
            // 字体大小
            textSize = 30f
            strokeWidth = 2f
        }

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.scale(1f, -1f)
        canvas.translate(0f, -measuredHeight.toFloat())
        canvas.drawColor(Color.BLACK)

        drawGrid(canvas)

        drawCurveLine(canvas)

        drawText(canvas)
    }


    private fun drawGrid(canvas: Canvas) {
        // 平行y轴
        val pathY = Path()
        pathY.moveTo(40f, 0f)
        pathY.rLineTo(0f, measuredHeight.toFloat())

        canvas.save()
        (0..measuredWidth / 40).forEach { _ ->
            canvas.drawPath(pathY, linePaint)
            canvas.translate(40f, 0f)
        }

        canvas.restore()

        // 平行x轴
        val pathX = Path()
        pathX.moveTo(0f, 40f)
        pathX.rLineTo(measuredWidth.toFloat(), 0f)
        canvas.save()
        (0..measuredHeight / 40).forEach { _ ->
            canvas.drawPath(pathX, linePaint)
            canvas.translate(0f, 40f)
        }
        canvas.restore()

    }


    private fun drawCurveLine(canvas: Canvas) {
        val curvePath = Path()
        // 折线
        pointList.forEach {
            curvePath.lineTo(it.x, it.y)
        }
        canvas.drawPath(curvePath, curvePaint)


        // 渐变色
        val last = pointList.last()
        pointList.forEach {
            curvePath.lineTo(it.x, it.y)
        }
        curvePath.lineTo(last.x, 0f)
        curvePath.close()
        curvePaint.style = Paint.Style.FILL
        curvePaint.shader = getShader()
        canvas.drawPath(curvePath, curvePaint)


        // 画点
        pointList.forEach {
            canvas.drawCircle(it.x, it.y, 15f, pointPaint)
        }
    }

    private fun getShader(): Shader {

        val shaderColors = intArrayOf(
            Color.argb(255, 250, 49, 33),
            Color.argb(165, 234, 115, 9),
            Color.argb(200, 32, 208, 88)
        )

        return LinearGradient(
            570f,
            520f,
            570f,
            0f,
            shaderColors,
            null,
            Shader.TileMode.CLAMP
        )

    }


    private fun drawText(canvas: Canvas) {

        pointList.forEachIndexed { index, point ->

            canvas.save()

            canvas.translate(point.x, point.y)
            canvas.scale(1f, -1f)
            canvas.rotate(10f)


            canvas.drawText(moneyList[index], 0f, 0f, textPaint)

            canvas.restore()

        }
    }

}