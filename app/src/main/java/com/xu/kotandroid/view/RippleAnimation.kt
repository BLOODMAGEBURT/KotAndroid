package com.xu.kotandroid.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
import kotlin.math.pow
import kotlin.math.sqrt


/**
 * @Author Xu
 * Date：2023/5/26 11:04
 * Description：
 */
@SuppressLint("ViewConstructor")
class RippleAnimation private constructor(
    context: Context,
    startX: Float,
    startY: Float,
    radius: Int
) : View(context) {

    //屏幕截图
    private var mBackground: Bitmap? = null
    private var mPaint: Paint? = null
    private var mMaxRadius = 0
    private var mStartRadius: Int = radius
    private var mCurrentRadius: Int = 0
    private var hasStarted = false
    private val mDuration: Long = 100L

    //扩散的起点
    private var mStartX = startX
    private var mStartY = startY

    //DecorView
    private var mRootView: ViewGroup? = null

    //    private val mOnAnimationEndListener: OnAnimationEndListener? = null
    private val mAnimatorListener: Animator.AnimatorListener? = null
    private val mAnimatorUpdateListener: AnimatorUpdateListener? = null

    init {
        //获取activity的根视图,用来添加本View
        mRootView = getActivityFromContext(context).window.decorView as ViewGroup
        mPaint = Paint()
        mPaint?.isAntiAlias = true
        //设置为擦除模式
        mPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        updateMaxRadius()
        initListener()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //在新的图层上面绘制
        val layer: Int = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        mBackground?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }
        canvas.drawCircle(mStartX, mStartY, mCurrentRadius.toFloat(), mPaint!!)
        canvas.restoreToCount(layer)
    }

    /**
     * 开始播放动画
     */
    fun start() {
        if (!hasStarted) {
            hasStarted = true
            updateBackground()
            attachToRootView()

            val valueAnimator =
                ValueAnimator.ofFloat(0f, mMaxRadius.toFloat()).apply {
                    duration = mDuration

                    doOnEnd {
//                        mOnAnimationEndListener?.onAnimationEnd()
                        hasStarted = false
                        //动画播放完毕, 移除本View
                        detachFromRootView()
                    }
                    addUpdateListener {
                        mCurrentRadius = (it.animatedValue as Float + mStartRadius).toInt()
                        postInvalidate()
                    }

                    start()
                }


            valueAnimator.addUpdateListener(mAnimatorUpdateListener)
            valueAnimator.addListener(mAnimatorListener)

        }
    }


    private fun initListener() {


    }

    private fun updateMaxRadius() {
        //将屏幕分成4个小矩形
        val leftTop = RectF(0f, 0f, mStartX + mStartRadius, mStartY + mStartRadius)
        val rightTop = RectF(leftTop.right, 0f, mRootView!!.right.toFloat(), leftTop.bottom)
        val leftBottom = RectF(0f, leftTop.bottom, leftTop.right, mRootView!!.bottom.toFloat())
        val rightBottom =
            RectF(leftBottom.right, leftTop.bottom, mRootView!!.right.toFloat(), leftBottom.bottom)
        //分别获取对角线长度
        val leftTopHypotenuse = sqrt(
            leftTop.width().toDouble().pow(2.0) + leftTop.height().toDouble().pow(2.0)
        )
        val rightTopHypotenuse = sqrt(
            rightTop.width().toDouble().pow(2.0) + rightTop.height().toDouble().pow(2.0)
        )
        val leftBottomHypotenuse = sqrt(
            leftBottom.width().toDouble().pow(2.0) + leftBottom.height().toDouble().pow(2.0)
        )
        val rightBottomHypotenuse = sqrt(
            rightBottom.width().toDouble().pow(2.0) + rightBottom.height().toDouble().pow(2.0)
        )
        //取最大值
        mMaxRadius = leftTopHypotenuse.coerceAtLeast(rightTopHypotenuse)
            .coerceAtLeast(leftBottomHypotenuse.coerceAtLeast(rightBottomHypotenuse)).toInt()

    }

    private fun getAnimator(): ValueAnimator? {
        val valueAnimator = ValueAnimator.ofFloat(0f, mMaxRadius.toFloat()).setDuration(mDuration)
        valueAnimator.addUpdateListener(mAnimatorUpdateListener)
        valueAnimator.addListener(mAnimatorListener)
        return valueAnimator
    }

    /**
     * 添加到根视图
     */
    private fun attachToRootView() {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mRootView?.addView(this)
    }

    /**
     * 从根视图中移除并释放资源
     */
    private fun detachFromRootView() {
        if (mRootView != null) {
            mRootView?.removeView(this)
            mRootView = null
        }
        if (mBackground != null) {
            if (mBackground?.isRecycled == false) {
                mBackground?.recycle()
            }
            mBackground = null
        }
        if (mPaint != null) {
            mPaint = null
        }
    }

    /**
     * 更新屏幕截图
     */
    private fun updateBackground() {
        if (mBackground != null && !mBackground!!.isRecycled) {
            mBackground?.recycle()
        }
        mRootView?.let {
            mBackground = getBitmapFromView(it)
        }
    }

    /**
     * 由canvas更新背景截图（drawingCache已废弃）
     */
    private fun getBitmapFromView(view: ViewGroup): Bitmap? {
//        view.measure(
//            MeasureSpec.makeMeasureSpec(view.layoutParams.width, MeasureSpec.EXACTLY),
//            MeasureSpec.makeMeasureSpec(view.layoutParams.height, MeasureSpec.EXACTLY)
//        )
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


    private fun getActivityFromContext(context: Context): Activity {
        var context1: Context? = context
        while (context1 is ContextWrapper) {
            if (context1 is Activity) {
                return context1
            }
            context1 = (context as ContextWrapper).baseContext
        }
        throw RuntimeException("Activity not found!")
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }


    companion object {

        fun create(onClickView: View): RippleAnimation {
            val context = onClickView.context
            val newWidth = onClickView.width / 2
            val newHeight = onClickView.height / 2
            //计算起点位置
            val startX: Float = getAbsoluteX(onClickView) + newWidth
            val startY: Float = getAbsoluteY(onClickView) + newHeight

            //起始半径
            //因为我们要避免遮挡按钮
            val radius = newWidth.coerceAtLeast(newHeight)
            return RippleAnimation(context, startX, startY, radius)
        }

        /**
         * 获取view在屏幕中的绝对x坐标
         */
        private fun getAbsoluteX(view: View): Float {
            var x = view.x
            val parent = view.parent
            if (parent is View) {
                x += getAbsoluteX(parent as View)
            }
            return x
        }


        /**
         * 获取view在屏幕中的绝对y坐标
         */
        private fun getAbsoluteY(view: View): Float {
            var y = view.y
            val parent = view.parent
            if (parent is View) {
                y += getAbsoluteY(parent as View)
            }
            return y
        }

    }
}