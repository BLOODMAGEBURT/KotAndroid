package com.xu.kotandroid.view

import android.content.Context
import android.graphics.PathEffect
import android.content.res.TypedArray
import com.xu.kotandroid.R
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet

/**
 * @Author yangsanning
 * @ClassName MiniFenShiConfig
 * @Description 迷你分时参数配置
 * @Date 2020/6/18
 */
class MiniFenShiConfig(context: Context, attrs: AttributeSet?) {
    /**
     * upColor: 涨颜色
     * downColor: 跌颜色
     * equalColor: 不涨不跌颜色
     * gradientBottomColor: 渐变底部颜色
     * strokeWidth: 实线宽
     * areaAlpha: 价格区域透明度
     * pathEffect: 路径效果
     */
    @JvmField
    var upColor = 0

    @JvmField
    var downColor = 0

    @JvmField
    var equalColor = 0
    var gradientBottomColor = 0
    var enableGradientBottom = false
    var strokeWidth = 0f
    var areaAlpha = 0
    var pathEffect: PathEffect? = null

    @JvmField
    var alwaysShowDottedLine = false

    /**
     * 当前颜色
     */
    @JvmField
    var currentColor = 0
    var priceAreaPaint: Paint? = null
    fun initAttr(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MiniFenShiView)
        upColor = typedArray.getColor(
            R.styleable.MiniFenShiView_upColor,
            context.resources.getColor(R.color.mini_fen_shi_up)
        )
        downColor = typedArray.getColor(
            R.styleable.MiniFenShiView_downColor,
            context.resources.getColor(R.color.mini_fen_shi_down)
        )
        equalColor = typedArray.getColor(
            R.styleable.MiniFenShiView_equalColor,
            context.resources.getColor(R.color.mini_fen_shi_equal)
        )
        gradientBottomColor = typedArray.getColor(
            R.styleable.MiniFenShiView_gradientBottomColor,
            context.resources.getColor(R.color.mini_fen_shi_gradient_bottom)
        )
        enableGradientBottom =
            typedArray.getBoolean(R.styleable.MiniFenShiView_enableGradientBottom, true)
        strokeWidth =
            typedArray.getDimensionPixelSize(R.styleable.MiniFenShiView_strokeWidth, 1).toFloat()
        areaAlpha = typedArray.getDimensionPixelSize(R.styleable.MiniFenShiView_areaAlpha, 150)
        val dottedWidth =
            typedArray.getDimensionPixelSize(R.styleable.MiniFenShiView_dottedLineWidth, 10)
        val dottedSpace =
            typedArray.getDimensionPixelSize(R.styleable.MiniFenShiView_dottedLineSpace, 5)
        pathEffect = DashPathEffect(floatArrayOf(dottedWidth.toFloat(), dottedSpace.toFloat()), 0F)
        alwaysShowDottedLine =
            typedArray.getBoolean(R.styleable.MiniFenShiView_alwaysShowDottedLine, false)
        typedArray.recycle()
    }

    fun initPaint() {
        priceAreaPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        priceAreaPaint!!.style = Paint.Style.FILL
        priceAreaPaint!!.alpha = areaAlpha
    }

    init {
        initAttr(context, attrs)
        initPaint()
    }
}