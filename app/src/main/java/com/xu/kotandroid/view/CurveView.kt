package com.xu.kotandroid.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
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
        // 禁用硬件加速
        setLayerType(LAYER_TYPE_HARDWARE, null)
        initPaint()
        initPath()
    }

    private fun initPath() {
        linePath = Path()
    }

    private val colorPath = Path()
    private lateinit var linePaint: Paint
    private lateinit var curvePaint: Paint
    private lateinit var colorPaint: Paint
    private lateinit var pointPaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var textBackPaint: Paint
    private lateinit var xPaint: Paint
    private lateinit var rPaint: Paint
    private lateinit var circlePaint: Paint
    private lateinit var rtPaint: Paint
    private lateinit var linePath: Path
    private val paddingStart = 80f
    private val paddingBottom = 80f
    private val paddingTop = 100f

    private val textRecList = mutableListOf<RectF>()


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
    private val xTitles = listOf("一月", "二月", "三月", "四月", "五月", "六月", "七月")
    private val yTitles = listOf("0w", "100w", "200w", "300w", "400w", "500w", "600w", "700w")

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
        colorPaint = Paint().apply {

            style = Paint.Style.FILL
            shader = makeShader()

        }
        textBackPaint = Paint().apply {

            style = Paint.Style.FILL
            color = Color.YELLOW
            isAntiAlias = true

        }

        xPaint = Paint().apply {

            style = Paint.Style.STROKE
            strokeWidth = 5f
            color = Color.YELLOW
            isAntiAlias = true

        }
        rPaint = Paint().apply {

            style = Paint.Style.FILL
            strokeWidth = 2f
            color = Color.WHITE
            setShadowLayer(5f, -5f, -5f, Color.GRAY)
            isAntiAlias = true

        }
        circlePaint = Paint().apply {

            style = Paint.Style.FILL
            strokeWidth = 2f
            color = Color.GREEN
            setShadowLayer(5f, -5f, -5f, Color.GRAY)
            isAntiAlias = true

        }
        rtPaint = Paint().apply {

            style = Paint.Style.FILL
//            strokeWidth = 2f
            textSize = 25f
            color = Color.BLACK
            isAntiAlias = true

        }

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.scale(1f, -1f)
        canvas.translate(paddingStart, -measuredHeight.toFloat() + paddingBottom)
        canvas.drawColor(Color.BLACK)

        drawGrid(canvas)

        drawCurveLine(canvas)

        drawText(canvas)

        // 坐标轴
        drawXAndY(canvas)
        drawXTitle(canvas)
        drawYTitle(canvas)
        // 右侧提示标注框
        drawRRect(canvas)

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
            colorPath.lineTo(it.x, it.y)
        }
        colorPath.lineTo(last.x, 0f)
        colorPath.close()
        canvas.drawPath(colorPath, colorPaint)


        // 画点
        pointList.forEach {
            canvas.drawCircle(it.x, it.y, 15f, pointPaint)
        }
    }

    private fun makeShader(): Shader {

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
            val rect = RectF(
                0f,
                -getTextHeight(textPaint),
                getTextWidth(moneyList[index]),
                0f
            )
            // 添加进点击区域
            textRecList.add(
                RectF(
                    point.x,
                    point.y,
                    point.x + getTextWidth(moneyList[index]),
                    point.y + getTextHeight(textPaint)
                )
            )
            // 绘制背景
            canvas.drawRoundRect(
                rect,
                10f,
                10f,
                textBackPaint
            )

            canvas.drawText(moneyList[index], 0f, 0f, textPaint)

            canvas.restore()

        }
    }

    private fun getTextHeight(paint: Paint): Float {
        val fontMetrics = paint.fontMetrics

        return fontMetrics.descent - fontMetrics.ascent + fontMetrics.leading
    }

    private fun getTextWidth(text: String): Float {
        return textPaint.measureText(text)
    }

    private fun drawXAndY(canvas: Canvas) {


        val xPath = Path()
        xPath.lineTo(measuredWidth - paddingStart - 20f, 0f)

        val arrowPath = Path()
        arrowPath.moveTo(measuredWidth - paddingStart - 40f, 20f)
        arrowPath.lineTo(measuredWidth - paddingStart - 20f, 0f)
        arrowPath.lineTo(measuredWidth - paddingStart - 40f, -20f)

        xPath.addPath(arrowPath)

        canvas.drawPath(xPath, xPaint)


        val yPath = Path()
        yPath.lineTo(0f, measuredHeight - paddingBottom - paddingTop)

        val yArrowPath = Path()
        yArrowPath.moveTo(-20f, measuredHeight - paddingBottom - 120f)
        yArrowPath.lineTo(0f, measuredHeight - paddingBottom - paddingTop)
        yArrowPath.lineTo(20f, measuredHeight - paddingBottom - 120f)

        yPath.addPath(yArrowPath)

        canvas.drawPath(yPath, xPaint)

    }

    private fun drawXTitle(canvas: Canvas) {

        val xPeriod = (measuredWidth - 100f) / 6


        xTitles.forEachIndexed { index, xTitle ->
            canvas.save()
            canvas.translate(xPeriod * index, -50f)
            canvas.scale(1f, -1f)
            canvas.drawText(xTitle, -textPaint.measureText(xTitle) / 2f, 0f, textPaint)
            canvas.restore()
        }

    }

    private fun drawYTitle(canvas: Canvas) {
        val yPeriod = (measuredHeight - paddingBottom - paddingTop) / 7

        yTitles.forEachIndexed { index, yTitle ->
            canvas.save()
            canvas.translate(0f, yPeriod * index)
            canvas.scale(1f, -1f)
            canvas.drawText(yTitle, -textPaint.measureText(yTitle) - 5f, 0f, textPaint)
            canvas.restore()
        }
    }


    private fun drawRRect(canvas: Canvas) {
        canvas.drawRoundRect(
            measuredWidth - paddingStart - 80f - 240f,
            measuredHeight - paddingStart - paddingBottom - 40f,
            measuredWidth - paddingStart - 80f,
            measuredHeight - paddingStart - paddingBottom - 40f - 150f,
            10f,
            10f,
            rPaint
        )

        canvas.drawCircle(
            measuredWidth - paddingStart - 80f - 220f,
            measuredHeight - paddingStart - paddingBottom - 60f,
            5f,
            circlePaint
        )


        canvas.save()

        canvas.translate(
            measuredWidth - paddingStart - 80f - 200f,
            measuredHeight - paddingStart - paddingBottom - 70f
        )
        canvas.scale(1f, -1f)

        canvas.drawText("总额：1230w", 0f, 0f, rtPaint)
        canvas.drawText("最大金额：520w", 0f, getTextHeight(rtPaint), rtPaint)

        canvas.restore()

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (event.action == MotionEvent.ACTION_DOWN) {
            return true
        }

        if (event.action == MotionEvent.ACTION_UP) {

            textRecList.forEachIndexed { index, rectF ->
                val contains =
                    rectF.contains(event.x - paddingStart, measuredHeight - event.y - paddingBottom)

                if (contains) {
                    Toast.makeText(context, moneyList[index], Toast.LENGTH_SHORT).show()
                }
            }
        }

        return super.onTouchEvent(event)
    }


}