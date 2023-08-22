package com.xu.kotandroid.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.xu.kotandroid.R


/**
 * @Author Xu
 * Date：2023/2/7 11:20
 * Description：
 */
class PorterDuffView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private var mX = -1f
    private var mY = -1f
    private var mWidth = 0
    private var mHeight = 0

    private var dst: Bitmap? = null
    private var src: Bitmap? = null

    private val clear = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    init {
        paint.apply {
            style = Paint.Style.FILL
        }

//        setLayerType(LAYER_TYPE_SOFTWARE, null)


    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w
        mHeight = h

        dst = makeDst(w, h)
        src = makeSrc(w, h, 0f)

        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val layerId = canvas.saveLayer(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), null)

        //先把手指轨迹画到目标Bitmap上
//        canvas.drawBitmap(dst, 100f, 100f, null)

        dst?.let {
            canvas.drawBitmap(it, 0f, 0f, paint)
        }
        paint.xfermode = clear

        src?.let {
            canvas.drawBitmap(it, 0f, 0f, paint)
        }

        paint.xfermode = null
        canvas.restoreToCount(layerId)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                mX = event.x
                mY = event.y
                anim()
//                invalidate()
                return true
            }

            MotionEvent.ACTION_UP -> {
                mX = -1f
                mY = -1f

            }

            else -> {}
        }

//        invalidate()
        return super.onTouchEvent(event)
    }

    private fun anim() {
        Log.e("here", "anim")
        ValueAnimator.ofFloat(0f, width * 3 / 8f).apply {
            duration = 2000
            addUpdateListener {
                val fraction = it.animatedValue as Float
                src = makeSrc(mWidth, mHeight, fraction)
                invalidate()
            }
            start()
        }

    }


    private fun makeDst(w: Int, h: Int): Bitmap {
//        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
//        val c = Canvas(bm)
//        val p = Paint(Paint.ANTI_ALIAS_FLAG)
//        //设置的透明度是FF，完全不透明
//        p.color = -0x33bc
//        c.drawOval(RectF(0f, 0f, w * 3 / 4f, h * 3 / 4f), p)

        val bm = BitmapFactory.decodeResource(resources, R.drawable.night_king_200x200, null)

        return bm
    }


    private fun makeSrc(w: Int, h: Int, r: Float): Bitmap? {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        //设置的透明度是FF
        p.color = -0x995501
//        c.drawRect(w / 3f, h / 3f, w * 19 / 20f, h * 19 / 20f, p)

//        c.drawOval(RectF(0f, 0f, w * 3 / 4f, h * 3 / 4f), p)

        c.drawCircle(w * 3 / 8f, h * 3 / 8f, r, p)

        return bm
    }


}