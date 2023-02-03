package com.xu.kotandroid.view

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.blankj.utilcode.util.AdaptScreenUtils
import com.xu.kotandroid.R
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * 饼状统计图，带有标注线，都可以自行设定其多种参数选项
 *
 *
 * Created By: Seal.Wu
 */
open class PieChartView : View {
    private var mTextPaint: TextPaint? = null
    private val mTextWidth = 0f
    private var mTextHeight = 0f

    /**
     * 饼图半径
     */
    private var pieChartCircleRadius = 100f
    private var textBottom = 0f

    /**
     * 记录文字大小
     */
    private var mTextSize = 14f

    /**
     * 饼图所占矩形区域（不包括文字）
     */
    private val pieChartCircleRectF = RectF()

    /**
     * 饼状图信息列表
     */
    private val pieceDataHolders: MutableList<PieceDataHolder> = ArrayList()

    /**
     * 标记线长度
     */
    private var markerLineLength = 30f

    /**
     * 内部圆环
     */
    var useInnerCircle = false
    var innerCirclePercent = 0.5f
    private var innerCircleColor = Color.WHITE


    private val innerTextSize by lazy {
        AdaptScreenUtils.pt2Px(16f).toFloat()
    }

    private val innerSubTextSize by lazy {
        AdaptScreenUtils.pt2Px(12f).toFloat()
    }


    constructor(context: Context?) : super(context) {
        init(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.PieChartView, defStyle, 0
        )
        pieChartCircleRadius = a.getDimension(
            R.styleable.PieChartView_circleRadius, pieChartCircleRadius
        )
        mTextSize = a.getDimension(
            R.styleable.PieChartView_textSize, mTextSize
        ) / resources.displayMetrics.density
        a.recycle()

        // Set up a default TextPaint object
        mTextPaint = TextPaint()
        mTextPaint!!.flags = Paint.ANTI_ALIAS_FLAG
        mTextPaint!!.textAlign = Paint.Align.LEFT


        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements()

    }

    private fun invalidateTextPaintAndMeasurements() {
        mTextPaint!!.textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, mTextSize, context.resources.displayMetrics
        )
        val fontMetrics = mTextPaint!!.fontMetrics
        mTextHeight = fontMetrics.descent - fontMetrics.ascent
        textBottom = fontMetrics.bottom
    }

    /**
     * 设置饼状图的半径
     *
     * @param pieChartCircleRadius 饼状图的半径（px）
     */
    fun setPieChartCircleRadius(pieChartCircleRadius: Int) {
        this.pieChartCircleRadius = pieChartCircleRadius.toFloat()
        invalidate()
    }

    /**
     * 设置标记线的长度
     *
     * @param markerLineLength 标记线的长度（px）
     */
    fun setMarkerLineLength(markerLineLength: Int) {
        this.markerLineLength = markerLineLength.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initPieChartCircleRectF()
        drawAllSectors(canvas)
        if (useInnerCircle) {
            drawInnerCircle(canvas)
            drawInnerText(canvas)

        }
    }


    private fun drawAllSectors(canvas: Canvas) {
        var sum = 0f
        for (pieceDataHolder in pieceDataHolders) {
            sum += pieceDataHolder.value
        }
        var sum2 = 0f
        for (pieceDataHolder in pieceDataHolders) {
            val startAngel = sum2 / sum * 360
            sum2 += pieceDataHolder.value
            val sweepAngel = pieceDataHolder.value / sum * 360
            drawSector(canvas, pieceDataHolder.color, startAngel, sweepAngel)
            drawMarkerLineAndText(
                canvas, pieceDataHolder.color, startAngel + sweepAngel / 2, pieceDataHolder.marker
            )
        }
    }

    private fun initPieChartCircleRectF() {
        pieChartCircleRectF.left = width / 2 - pieChartCircleRadius
        pieChartCircleRectF.top = height / 2 - pieChartCircleRadius
        pieChartCircleRectF.right = pieChartCircleRectF.left + pieChartCircleRadius * 2
        pieChartCircleRectF.bottom = pieChartCircleRectF.top + pieChartCircleRadius * 2
    }
    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.(sp)
     */
    /**
     * Sets the view's text dimension attribute value. In the PieChartView view, this dimension
     * is the font size.
     *
     * @param textSize The text dimension attribute value to use.(sp)
     */
    var textSize: Float
        get() = mTextSize
        set(textSize) {
            mTextSize = textSize
            invalidateTextPaintAndMeasurements()
        }

    /**
     * 设置饼状图要显示的数据
     *
     * @param data 列表数据
     */
    fun setData(data: List<PieceDataHolder>?) {
        if (data != null) {
            pieceDataHolders.clear()
            pieceDataHolders.addAll(data)
        }
        invalidate()
    }

    /**
     * 绘制扇形
     *
     * @param canvas     画布
     * @param paintColor      要绘制扇形的颜色
     * @param startAngle 起始角度
     * @param sweepAngle 结束角度
     */
    private fun drawSector(canvas: Canvas, paintColor: Int, startAngle: Float, sweepAngle: Float) {
        val paint = Paint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            style = Paint.Style.FILL
            color = paintColor
        }

        canvas.drawArc(pieChartCircleRectF, startAngle, sweepAngle, true, paint)
    }

    /**
     * 绘制标注线和标记文字
     *
     * @param canvas      画布
     * @param color       标记的颜色
     * @param rotateAngel 标记线和水平相差旋转的角度
     */
    private fun drawMarkerLineAndText(
        canvas: Canvas, color: Int, rotateAngel: Float, text: String?
    ) {
        val paint = Paint()
        paint.flags = Paint.ANTI_ALIAS_FLAG
        paint.style = Paint.Style.STROKE
        paint.color = color
        val path = Path()
        path.close()
        path.moveTo((width / 2).toFloat(), (height / 2).toFloat())
        val x = (width / 2 + (markerLineLength + pieChartCircleRadius) * cos(
            Math.toRadians(rotateAngel.toDouble())
        )).toFloat()
        val y = (height / 2 + (markerLineLength + pieChartCircleRadius) * sin(
            Math.toRadians(rotateAngel.toDouble())
        )).toFloat()
        path.lineTo(x, y)
        val landLineX: Float = if (270f > rotateAngel && rotateAngel > 90f) {
            x - 20
        } else {
            x + 20
        }
        path.lineTo(landLineX, y)
        canvas.drawPath(path, paint)
        mTextPaint!!.color = color
        if (270f > rotateAngel && rotateAngel > 90f) {
            val textWidth = mTextPaint!!.measureText(text)
            canvas.drawText(
                text!!, landLineX - textWidth, y + mTextHeight / 2 - textBottom, mTextPaint!!
            )
        } else {
            canvas.drawText(text!!, landLineX, y + mTextHeight / 2 - textBottom, mTextPaint!!)
        }
    }


    /**
     * 绘制内部圆环
     */
    private fun drawInnerCircle(canvas: Canvas) {
        val innerPaint = Paint().apply {
            style = Paint.Style.FILL
            color = innerCircleColor
        }

        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            pieChartCircleRadius * innerCirclePercent,
            innerPaint
        )
    }


    /**
     * 绘制内部文字
     */
    private fun drawInnerText(canvas: Canvas) {
        val xCenter = (width / 2).toFloat()
        val yCenter = (height / 2).toFloat()
        val paint = Paint().apply {
            style = Paint.Style.FILL
            textSize = innerTextSize
            color = Color.CYAN
        }

        val subPaint = Paint().apply {
            style = Paint.Style.FILL
            textSize = innerSubTextSize
            color = Color.RED
        }
        /**
         * 转移之前保存canvas
         */
        canvas.save()
        canvas.translate(xCenter, yCenter)

        canvas.drawLine(0f, 0f, 100f, 0f, paint)
        canvas.drawLine(0f, 0f, 0f, 100f, paint)

        val textWidth = paint.measureText("总流入")
        val subTextWidth = subPaint.measureText("123.90")


        /**
         * baseline本来在Y0处，需要把新的baseline设置为（旧baseline 和 文字中心点之间的距离），这样，文字中心点在Y0,此时文字垂直居中
         * 而 旧baseline 和 文字中心点之间的距离 = 文字高度的一般 减去 descent
         * val baselineY = (paint.descent() -paint.ascent()) /2 -paint.descent()
         * 可推导出下面的公式
         */
        val baselineY = abs(paint.ascent() + paint.descent()) / 2


        canvas.drawText("总流入", -textWidth / 2, -paint.descent(), paint)

        canvas.drawText("123.90", -subTextWidth / 2, abs(subPaint.ascent()), subPaint)


        /**
         * 操作之后恢复canvas
         */
        canvas.restore()
    }

    /**
     * 饼状图每块的信息持有者
     */
    class PieceDataHolder(
        /**
         * 每块扇形的值的大小
         */
        val value: Float,
        /**
         * 扇形的颜色
         */
        val color: Int,
        /**
         * 每块的标记
         */
        val marker: String
    )
}