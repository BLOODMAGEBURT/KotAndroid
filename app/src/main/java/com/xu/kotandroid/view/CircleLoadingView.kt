package com.xu.kotandroid.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.xu.kotandroid.R
import kotlin.math.abs
import kotlin.math.atan2

/**
 * @Author Xu
 * Date：2023/2/14 13:26
 * Description：路径绘制 loading
 */
class CircleLoadingView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttrs: Int = 0
) :
    View(context, attributeSet, defAttrs) {

    private var mWidth = 0f
    private var mHeight = 0f
    private var radius = 0f


    private var animValue = 0f
    private var anim: ValueAnimator? = null
    private val animDuration = 2000L

    private val paint = Paint()
    private val path = Path()
    private val pathMeasure = PathMeasure()

    private val arrow = BitmapFactory.decodeResource(resources, R.drawable.arrow_yellow)

    private val dst = Path()

    private val bitmapMatrix = Matrix()
    private val bitmapPaint = Paint()

    init {
        initPaint()

        initAnim()
    }

    private fun initAnim() {
        anim = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = animDuration
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                animValue = (it.animatedValue as Float)
                invalidate()
            }
            start()
        }
    }

    private fun initPath() {
        path.apply {
            reset()
            addCircle(0f, 0f, radius, Path.Direction.CW)
        }
    }

    private fun initPaint() {
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            color = Color.CYAN
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        radius = minOf(w, h) / 2f - arrow.width / 2f
        super.onSizeChanged(w, h, oldw, oldh)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(mWidth / 2, mHeight / 2)
//        canvas.rotate(45f)
        drawPath(canvas)

    }

    private fun drawPath(canvas: Canvas) {
        initPath()
        pathMeasure.setPath(path, false)
        val length = pathMeasure.length
        dst.reset()
        val stop = length * animValue
        val start = stop - ((0.5f - abs(animValue - 0.5f)) * length)
        val pos = FloatArray(2)
        val tan = FloatArray(2)
        pathMeasure.getPosTan(stop, pos, tan)
        pathMeasure.getSegment(start, stop, dst, true)
        canvas.drawPath(dst, paint)

        // 方式一： 弧度 换算 成角度
//        val degree = atan2(tan[1], tan[0]) * 180 / Math.PI
//        bitmapMatrix.reset()
//        bitmapMatrix.preTranslate(pos[0] - arrow.width / 2f, pos[1] - arrow.height / 2f)
//        bitmapMatrix.preRotate(degree.toFloat(), arrow.width / 2f, arrow.height / 2f)

        // 方式二： 直接使用 getMatrix()获取当前位置的矩阵信息
        bitmapMatrix.reset()
        pathMeasure.getMatrix(
            stop,
            bitmapMatrix,
            PathMeasure.TANGENT_MATRIX_FLAG or PathMeasure.POSITION_MATRIX_FLAG
        )
        bitmapMatrix.preTranslate(-arrow.width / 2f, -arrow.height / 2f)
        canvas.drawBitmap(arrow, bitmapMatrix, bitmapPaint)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        anim?.cancel()
    }

}