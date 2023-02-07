package com.xu.kotandroid.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * @Author Xu
 * Date：2023/2/7 11:20
 * Description：
 */
class ContainsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val paint2 = Paint()
    private val rect = RectF(100f, 100f, 300f, 300f)
    private var mX = -1f
    private var mY = -1f
    private var mWidth = 0
    private var mHeight = 0

    private val path = Path()
    private val path2 = Path()

    init {
        paint.apply {
            style = Paint.Style.FILL
        }
        paint2.apply {
            style = Paint.Style.STROKE
            color = Color.CYAN
        }
        path.apply {
            addRect(rect, Path.Direction.CW)

        }

        path2.apply {
            addCircle(300f, 300f, 100f, Path.Direction.CW)
        }

        path.op(path2, Path.Op.DIFFERENCE)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w
        mHeight = h
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = if (rect.contains(mX, mY)) Color.GREEN else Color.RED
//        canvas.translate(mWidth / 2f, mHeight / 2f)
        canvas.drawPath(path, paint)
//        canvas.drawPath(path2, paint)

        canvas.drawPath(path2, paint2)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                mX = event.x
                mY = event.y
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                mX = -1f
                mY = -1f

            }
            else -> {}
        }

        invalidate()
        return super.onTouchEvent(event)
    }


}