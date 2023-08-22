package com.xu.kotandroid.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PixelFormat
import android.graphics.PointF
import android.graphics.drawable.Drawable
import kotlin.math.cos
import kotlin.math.sin

/**
 * @Author Xu
 * Date：2023/5/30 14:56
 * Description：
 */
class ArrowDrawable(private val mWidth: Int, private val mHeight: Int) : Drawable() {


    private var mPaint = Paint()

    private val mCenterX = mWidth / 2f
    private val mCenterY = mHeight / 2f

    //bow
    private val bowPath = Path()
    private val mTempPoint = PointF()
    private var mBowLength = mWidth / 3

    //arrow
    private val arrowPath = Path()
    private val maxOffset = mWidth / 16


    //arrow string
    private val mStringStartPoint = PointF()
    private val mStringEndPoint = PointF()
    private val mStringMiddlePoint = PointF()
    private val pathMeasure = PathMeasure()
    private var initMiddleY = 0f


    init {
        initPaint()
        initArrowPath()
    }

    private fun initPaint() {
        mPaint.apply {
            color = Color.RED
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            isAntiAlias = true
        }

    }

    private fun initArrowPath() {
        val endPointF = getPointByAngle(30f)
        arrowPath.moveTo(mCenterX, endPointF.y)
        arrowPath.lineTo(mCenterX, endPointF.y - maxOffset - 60f)
    }


    override fun draw(canvas: Canvas) {

        canvas.save()
        canvas.translate(0f, 50f)

        // 弓
        drawArrow(canvas)
        // 弦
        drawString(canvas)
        // 箭
        mPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        canvas.drawPath(arrowPath, mPaint)
        canvas.restore()
    }


    private fun drawArrow(canvas: Canvas) {
        mPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        canvas.drawPath(bowPath, mPaint)
    }


    private fun drawString(canvas: Canvas) {
        mPaint.apply {
            style = Paint.Style.FILL
            strokeWidth = 2f
        }

        canvas.drawLine(
            mStringStartPoint.x,
            mStringStartPoint.y,
            mStringMiddlePoint.x,
            mStringMiddlePoint.y,
            mPaint
        )
        canvas.drawLine(
            mStringMiddlePoint.x,
            mStringMiddlePoint.y,
            mStringEndPoint.x,
            mStringEndPoint.y,
            mPaint
        )
    }


    private fun updateStringPoint(progress: Int) {
        pathMeasure.setPath(bowPath, false)
        val length = pathMeasure.length
        val pos = FloatArray(2)
        val tan = FloatArray(2)
        pathMeasure.getPosTan(length * 0.05f, pos, tan)
        mStringStartPoint.set(pos[0], pos[1])
        pathMeasure.getPosTan(length * 0.95f, pos, tan)
        mStringEndPoint.set(pos[0], pos[1])

        if (progress == 0) {
            initMiddleY = pos[1]
        }

        mStringMiddlePoint.x = mCenterX
        mStringMiddlePoint.y = initMiddleY + (maxOffset) * progress / 100f


    }

    private fun updateBowPath(angel: Float) {

        val endPointF = getPointByAngle(angel)
        //起始点坐标 startX =  endPointF.x - 2*(endPointF.x - mCenterX)
        val startX = mCenterX * 2 - endPointF.x
        val startY = endPointF.y
        //控制点坐标  控制点的y坐标，刚好跟两端的y坐标相反，这样的话，线条的中点位置就能保持不变
        val controlX = mCenterX
        val controlY = -endPointF.y

        bowPath.reset()
        //根据三点坐标画一条二届贝塞尔曲线
        bowPath.moveTo(startX, startY)
        bowPath.quadTo(controlX, controlY, endPointF.x, endPointF.y)


    }

    /**
     * 设置弓的弯曲程度
     *
     * @param progress 弓的弯曲程度，取值范围[0,100]
     */
    fun setProgress(progress: Int) {
        updateBowPath(30 + progress.toFloat() / 100f * 15)

        updateStringPoint(progress)

        updateArrowPath(progress)
        invalidateSelf()
    }

    private var mArrowOffset = -1f
    private fun updateArrowPath(progress: Int) {

        val offset = maxOffset * progress / 100f

        // 先重置
        if (mArrowOffset >= 0f) {
            arrowPath.offset(0f, -mArrowOffset)
        }
        // 再用新的
        arrowPath.offset(0f, offset)

        mArrowOffset = offset
    }

    /**
     * 根据弓当前弯曲的角度计算新的端点坐标
     *
     * @param angle 弓当前弯曲的角度
     * @return 新的端点坐标
     */
    private fun getPointByAngle(angle: Float): PointF {
        //先把角度转成弧度
        val radian = angle * Math.PI / 180
        //半径 取 弓长的一半
        val radius = mBowLength / 2
        //x轴坐标值
        val x = (mCenterX + radius * cos(radian)).toFloat()
        //y轴坐标值
        val y = (radius * sin(radian)).toFloat()
        mTempPoint.set(x, y)
        return mTempPoint
    }

    override fun getIntrinsicWidth(): Int {
        return mWidth
    }

    override fun getIntrinsicHeight(): Int {
        return mHeight
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("PixelFormat.TRANSLUCENT", "android.graphics.PixelFormat")
    )
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}