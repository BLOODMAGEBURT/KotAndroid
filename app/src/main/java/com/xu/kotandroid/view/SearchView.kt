package com.xu.kotandroid.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import kotlin.math.abs

/**
 * @Author Xu
 * Date：2023/2/9 13:21
 * Description：
 */
class SearchView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttrs: Int = 0
) :
    View(context, attributeSet, defStyleAttrs) {
    private var mWidth = 0
    private var mHeight = 0
    private var radius = 0f

    private var currentState = SearchState.NONE
    private var mAnimatorValue = 0f

    private val paint = Paint()
    private val searchPath = Path()
    private val circlePath = Path()
    private val pathMeasure = PathMeasure()

    private var searchingAnim: ValueAnimator? = null
    private var startAnim: ValueAnimator? = null
    private var doneAnim: ValueAnimator? = null

    init {
        initPaint()
//        initPath()
    }

    private fun initPath() {

        val r2 = radius / 2

        val r = r2 / 2.5f
        searchPath.addArc(RectF(-r, -r, r, r), 45f, 359.9f)

        circlePath.addArc(RectF(-r2, -r2, r2, r2), 45f, -359.9f)

        pathMeasure.setPath(circlePath, false)
        val pos = FloatArray(2)

        pathMeasure.getPosTan(0f, pos, null)

        searchPath.lineTo(pos[0], pos[1])
    }

    private fun initPaint() {
        paint.apply {
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeWidth = 5f
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.translate(mWidth / 2f, mHeight / 2f)
        drawSearch(canvas)
    }

    private val dst = Path()
    private fun drawSearch(canvas: Canvas) {
        initPath()
        when (currentState) {
            SearchState.NONE -> canvas.drawPath(searchPath, paint)
            SearchState.STARTING -> {
                pathMeasure.setPath(searchPath, false)
                dst.reset()
                val length = pathMeasure.length
                pathMeasure.getSegment(length * mAnimatorValue, length, dst, true)
                canvas.drawPath(dst, paint)
            }
            SearchState.SEARCHING -> {
                pathMeasure.setPath(circlePath, false)
                dst.reset()
                val length = pathMeasure.length
                val stop = length * mAnimatorValue
                val start = stop - ((0.5f - abs(mAnimatorValue - 0.5f)) * length)
                pathMeasure.getSegment(start, stop, dst, true)
                canvas.drawPath(dst, paint)
            }
            SearchState.DONE -> {
                pathMeasure.setPath(searchPath, false)
                dst.reset()
                val length = pathMeasure.length
                pathMeasure.getSegment(length * mAnimatorValue, length, dst, true)
                canvas.drawPath(dst, paint)
            }
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h

        radius = minOf(w, h) / 2f
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (event.action == MotionEvent.ACTION_DOWN) {
            Log.d("touch11", "onTouchEvent Down")
            return true
        } else if (
            event.action == MotionEvent.ACTION_UP
        ) {
            Log.d("touch11", "onTouchEvent UP")
            performClick()
            return true
        }

        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        doAnim()
        return super.performClick()
    }

    private fun doAnim() {

        if (startAnim?.isRunning == true) {
            return
        }

        if (doneAnim?.isRunning == true) {
            return
        }

        if (searchingAnim?.isRunning == true) {
            searchDone()
            return
        }

        startAnim = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1500
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                mAnimatorValue = (it.animatedValue as Float)
                invalidate()
            }
            doOnStart {
                currentState = SearchState.STARTING
            }
            doOnEnd {
                currentState = SearchState.SEARCHING
                searchingAnim = ValueAnimator.ofFloat(0f, 1f).apply {
                    duration = 1500
                    interpolator = AccelerateDecelerateInterpolator()
                    repeatCount = ValueAnimator.INFINITE
                    addUpdateListener {
                        mAnimatorValue = (it.animatedValue as Float)
                        invalidate()
                    }
                    start()
                }
            }
            start()
        }

    }


    /**
     * 主动关闭搜索
     */
    fun searchDone() {
        if (searchingAnim?.isRunning == true) {
            searchingAnim?.cancel()
            currentState = SearchState.DONE
            doneAnim = ValueAnimator.ofFloat(1f, 0f).apply {
                duration = 1500
                addUpdateListener {
                    mAnimatorValue = (it.animatedValue as Float)
                    invalidate()
                }
                doOnEnd {
                    currentState = SearchState.NONE
                }
                start()
            }
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        startAnim?.cancel()
        searchingAnim?.cancel()
        doneAnim?.cancel()
    }

}

enum class SearchState {
    NONE, STARTING, SEARCHING, DONE
}
