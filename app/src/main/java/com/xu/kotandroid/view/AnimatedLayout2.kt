package com.xu.kotandroid.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.xu.kotandroid.R

class AnimatedLayout2 : ConstraintLayout {
    private var mBackgroundImg: ImageView? = null
    private var mTitleTV: TextView? = null
    private var mProfileCircleIV: ImageView? = null
    private var mOffsetValue = -1f
    private var mIdleState = WINTER_STATE
    private var mCurrentAnimation = IDLE_ANIMATION_STATE
    private var mPreviousTouchX = -1f
    private var mPath: Path? = null
    private var mScreenShotBitmap: Bitmap? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initIdleState(w)
        mScreenShotBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mBackgroundImg = findViewById(R.id.background_image)
        mTitleTV = findViewById(R.id.title_tv)
        mProfileCircleIV = findViewById(R.id.profile_image)
    }

    private fun init() {
        mPath = Path()
    }

    private fun initIdleState(w: Int) {
        val initOffset = (if (mIdleState == SUMMER_STATE) w else 0).toFloat()
        updateOffset(initOffset)
        if (mIdleState == WINTER_STATE) {
            applyWinter()
        } else {
            applySummer()
        }
    }

    private fun updateOffset(newOffsetValue: Float) {
        if (mOffsetValue == newOffsetValue) {
//            nothing to do
            return
        }
        mOffsetValue = newOffsetValue
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mPreviousTouchX = event.x
                true
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - mPreviousTouchX
                mPreviousTouchX = event.x
                if (dx == 0f) {
                    return false
                }
                if (!isScrollingPossible(dx)) {
                    return false
                }
                val newOffset: Float = if (dx > 0) {
                    if (mOffsetValue + dx > width) width.toFloat() else mOffsetValue + dx
                } else {
                    if (mOffsetValue + dx < 0) 0f else mOffsetValue + dx
                }
                if (mCurrentAnimation == IDLE_ANIMATION_STATE) {
                    performScreenShot()
                    when (mOffsetValue) {
                        width.toFloat() -> {
                            applyWinter()
                        }

                        0f -> {
                            applySummer()
                        }

                        else -> {
                            throw IllegalStateException(
                                "Offset value has " +
                                        "to be only 0 or equals layout width during" +
                                        " IDLE_ANIMATION_STATE"
                            )
                        }
                    }
                    mCurrentAnimation = if (dx > 0) FROM_LEFT_TO_RIGHT else FROM_RIGHT_TO_LEFT
                }
                updateOffset(newOffset)
                invalidate()
                true
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                mCurrentAnimation = IDLE_ANIMATION_STATE
                mPreviousTouchX = -1f
                if (mOffsetValue < width / 2f) {
                    mIdleState = WINTER_STATE
                    updateOffset(0f)
                    applyWinter()
                } else {
                    mIdleState = SUMMER_STATE
                    updateOffset(width.toFloat())
                    applySummer()
                }
                invalidate()
                true
            }

            else -> false
        }
    }

    override fun onDraw(canvas: Canvas) {
        setWillNotDraw(false)
        if (canvas is ScreenShotCanvas) {
            super.onDraw(canvas)
            return
        }
        if (mCurrentAnimation != IDLE_ANIMATION_STATE) {
            displayScreenShot(canvas)
        }
    }

    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        if (canvas is ScreenShotCanvas) {
            return super.drawChild(canvas, child, drawingTime)
        }
        return if (mCurrentAnimation != IDLE_ANIMATION_STATE) {
            canvas.save()
            clipToRectangle(canvas, child)
            val result = super.drawChild(canvas, child, drawingTime)
            canvas.restore()
            result
        } else {
            super.drawChild(canvas, child, drawingTime)
        }
    }

    private fun performScreenShot() {
        val canvas: Canvas = ScreenShotCanvas(mScreenShotBitmap)
        draw(canvas)
    }

    private fun isScrollingPossible(dx: Float): Boolean {
        require(dx != 0f) { "dx can not be 0 in this block" }
        return ((dx <= 0 || canScrollToRight(dx))
                && (dx >= 0 || canScrollToLeft(dx)))
    }

    private fun canScrollToRight(dx: Float): Boolean {
        return mOffsetValue < width
    }

    private fun canScrollToLeft(dx: Float): Boolean {
        return mOffsetValue > 0
    }

    private fun displayScreenShot(canvas: Canvas) {
        canvas.drawBitmap(mScreenShotBitmap!!, 0f, 0f, null)
    }

    private fun clipToRectangle(canvas: Canvas, child: View) {
        mPath!!.reset()
        if (mCurrentAnimation == FROM_LEFT_TO_RIGHT) {
            mPath!!.addRect(
                0f,
                0f,
                mOffsetValue,
                height.toFloat(),
                Path.Direction.CW
            )
            canvas.clipPath(mPath!!)
        } else if (mCurrentAnimation == FROM_RIGHT_TO_LEFT) {
            mPath!!.addRect(
                mOffsetValue,
                0f,
                width.toFloat(),
                height.toFloat(),
                Path.Direction.CW
            )
            canvas.clipPath(mPath!!)
        }
    }

    //region Methods to change Ui elements attributes for summer or winter style
    private fun applySummer() {
        applySummerImageBackground()
        applySummerTitleTV()
        applySummerProfile()
    }

    private fun applyWinter() {
        applyWinterImageBackground()
        applyWinterTitleTV()
        applyWinterProfile()
    }

    private fun applySummerImageBackground() {
        mBackgroundImg!!.setImageResource(R.drawable.summer_550_309)
    }

    private fun applyWinterImageBackground() {
        mBackgroundImg!!.setImageResource(R.drawable.winter_550_309)
    }

    private fun applySummerTitleTV() {
        mTitleTV!!.setBackgroundResource(R.drawable.summer_text_background)
        mTitleTV!!.setTextColor(Color.BLACK)
        mTitleTV!!.text = "WINTER IS COMING..."

        // fix bug: text view with wrap_content layout param
        // does not resize its width and
        // last word of new text will not displayed if new string is longer
        mTitleTV!!.post { mTitleTV!!.requestLayout() }
    }

    private fun applyWinterTitleTV() {
        mTitleTV!!.setBackgroundResource(R.drawable.winter_text_background)
        mTitleTV!!.setTextColor(Color.WHITE)
        mTitleTV!!.text = "WINTER IS HERE..."
        mTitleTV!!.post { mTitleTV!!.requestLayout() }
    }

    private fun applySummerProfile() {
//        mProfileCircleIV!!.borderColor = Color.WHITE
        mProfileCircleIV!!.setImageResource(R.drawable.john_snow_200_200)
    }

    private fun applyWinterProfile() {
//        mProfileCircleIV!!.borderColor = Color.BLACK
        mProfileCircleIV!!.setImageResource(R.drawable.night_king_200x200)
    }

    //endregion
    private class ScreenShotCanvas constructor(bitmap: Bitmap?) : Canvas(
        bitmap!!
    )

    companion object {
        private const val SUMMER_STATE = 1
        private const val WINTER_STATE = 2
        private const val FROM_LEFT_TO_RIGHT = 1
        private const val FROM_RIGHT_TO_LEFT = 2
        private const val IDLE_ANIMATION_STATE = 3
    }
}