package com.xu.kotandroid.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.xu.kotandroid.bean.FenShiBean
import com.xu.kotandroid.bean.ViewPoint

/**
 * @Author Xu
 * Date：2022/1/11 11:12 上午
 * Description：
 */
class MiniFenShiView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    private var viewWidth = 0
    private var viewHeight = 0

    private var config: MiniFenShiConfig = MiniFenShiConfig(context!!, attrs)
    private var dataManager: MiniFenShiDataManager = MiniFenShiDataManager(config)
    private lateinit var lastClosePaint: Paint
    private lateinit var curvePaint: Paint
    private lateinit var colorPaint: Paint

    private var shaderColors = intArrayOf(
        Color.argb(80, 235, 66, 66),
        Color.argb(50, 235, 66, 66),
        Color.argb(20, 235, 66, 66)
    )


    private val curvePath = Path()
    private val colorPath = Path()

    init {
        initPaint()
    }


    private fun initPaint() {
        lastClosePaint = Paint()
        lastClosePaint.apply {
//            color = config.currentColor
            color = Color.GRAY
            strokeWidth = config.strokeWidth
            pathEffect = config.pathEffect
        }

        curvePaint = Paint().apply {

            style = Paint.Style.STROKE
            color = Color.RED
            strokeWidth = 1f
        }


        colorPaint = Paint().apply {
            style = Paint.Style.FILL
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.scale(1f, -1f)
        canvas.translate(0f, -viewHeight.toFloat())
        // 绘制昨日收盘价线
        if (dataManager.preClose in dataManager.extremum.minimum..dataManager.extremum.maximum) {
            drawLastClose(canvas, dataManager.preCloseY)
        }

        // 绘制价格折线
        if (dataManager.points.isNotEmpty()) {
            drawPriceLine(canvas)
        }


    }


    private fun drawLastClose(canvas: Canvas, lastClose: Float) {
        canvas.drawLine(
            0f,
            lastClose,
            measuredWidth.toFloat(),
            lastClose,
            lastClosePaint
        )
    }


    private fun drawPriceLine(canvas: Canvas) {


        // 折线
        dataManager.points.forEachIndexed { index, it ->
            if (index == 0) {
                curvePath.moveTo(it.x, it.y)
            } else {
                curvePath.lineTo(it.x, it.y)
            }
        }
        canvas.drawPath(curvePath, curvePaint)
        curvePath.reset()


        // 渐变色阴影
        val last = dataManager.points.last()
        dataManager.points.forEach {

            colorPath.lineTo(it.x, it.y)
        }
        colorPath.lineTo(last.x, 0f)
        colorPaint.shader = makeShader(
            dataManager.points.maxByOrNull { it.y }!!
        )
        canvas.drawPath(colorPath, colorPaint)
        colorPath.reset()
    }


    private fun makeShader(heightPoint: ViewPoint): Shader {

        return LinearGradient(
            heightPoint.x,
            heightPoint.y,
            heightPoint.x,
            0f,
            shaderColors,
            null,
            Shader.TileMode.CLAMP
        )

    }


    fun setNewData(fenShi: FenShiBean) {
        post {
            dataManager.setNewData(fenShi)
            dataManager.calculatePointList(viewWidth, viewHeight, fenShi)
            invalidate()
        }
    }

    /**
     * 重置当前颜色
     */
    fun resetCurrentMode(mode: String) {

        when (mode) {
            "up" -> {
                curvePaint.color = Color.RED
                shaderColors = intArrayOf(
                    Color.argb(80, 235, 66, 66),
                    Color.argb(20, 235, 66, 66)
                )
            }
            "down" -> {
                curvePaint.color = Color.GREEN
                shaderColors = intArrayOf(
                    Color.argb(80, 42, 189, 110),
                    Color.argb(20, 42, 189, 110)
                )
            }
            "flat" -> {
                curvePaint.color = Color.GRAY
                shaderColors = intArrayOf(
                    Color.argb(80, 190, 190, 190),
                    Color.argb(20, 190, 190, 190)
                )
            }
        }
        invalidate()
    }

}