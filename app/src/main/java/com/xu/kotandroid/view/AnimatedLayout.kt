package com.xu.kotandroid.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import coil.transform.CircleCropTransformation
import com.xu.kotandroid.R


/**
 * @Author Xu
 * Date：2023/5/23 11:01
 * Description：
 */
class AnimatedLayout : ConstraintLayout {


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

    // child views
    private var mBackgroundImg: ImageView? = null
    private var mWeatherIcon: ImageView? = null
    private var mTitleTV: TextView? = null
    private var mProfileCircleIV: ImageView? = null

    //main parameter which defines the ui state of the views during transformation
    // and the width of the Path for clipping
    private var mOffsetValue = -1f

    //path for clipping. Is used to make only specified area to be drawn and visible
    private var mPath: Path? = null
    private var mScreenShotBitmap: Bitmap? = null


    //current layout state
    private var mIdleState = WINTER_STATE

    private var mCurrentAnimation = IDLE_ANIMATION_STATE
    private var mPreviousTouchX = 0f


    private fun init() {
        mPath = Path()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initIdleState(w)
        mScreenShotBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565)
    }


    override fun onFinishInflate() {
        super.onFinishInflate()

        mBackgroundImg = findViewById(com.xu.kotandroid.R.id.background_image)
        mWeatherIcon = findViewById(com.xu.kotandroid.R.id.weatherIconIV)
        mTitleTV = findViewById(com.xu.kotandroid.R.id.title_tv)
        mProfileCircleIV = findViewById(com.xu.kotandroid.R.id.profile_image)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mPreviousTouchX = event.x
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - mPreviousTouchX
                mPreviousTouchX = event.x

                if (dx == 0f) return false
                if (dx > 0 && !canScrollToRight()) return false
                if (dx < 0 && !canScrollToLeft()) return false

                val newOffset = if (dx > 0) {
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
                            Log.e("here", "offset: $mOffsetValue")
                        }
                    }
                    mCurrentAnimation = if ((dx > 0)) FROM_LEFT_TO_RIGHT else FROM_RIGHT_TO_LEFT
                }

                updateOffset(newOffset)
                invalidate()
                return true
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
                return true
            }

            else -> {
                return false
            }
        }
    }


    override fun drawChild(canvas: Canvas, child: View?, drawingTime: Long): Boolean {

        if (canvas is ScreenShotCanvas) {

            if (isChildExcluded(child)) {
                return false
            }

            return super.drawChild(canvas, child, drawingTime)
        }

        if (mCurrentAnimation != IDLE_ANIMATION_STATE) {
            if (isChildExcluded(child)) {
                return super.drawChild(canvas, child, drawingTime)
            }
            canvas.save()
            clipToRectangle(canvas)
            val result = super.drawChild(canvas, child, drawingTime)
            canvas.restore()
            return result
        }

        return super.drawChild(canvas, child, drawingTime)
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

    private fun displayScreenShot(canvas: Canvas) {
        mScreenShotBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }
    }

    private fun clipToRectangle(canvas: Canvas) {
        mPath?.reset()

        if (mCurrentAnimation == FROM_LEFT_TO_RIGHT) {
            mPath?.addRect(0f, 0f, mOffsetValue, height.toFloat(), Path.Direction.CW)
        } else if (mCurrentAnimation == FROM_RIGHT_TO_LEFT) {
            mPath?.addRect(mOffsetValue, 0f, width.toFloat(), height.toFloat(), Path.Direction.CW)
        }
        canvas.clipPath(mPath!!)
    }

    private fun performScreenShot() {
        mScreenShotBitmap?.let {

            val canvas: Canvas = ScreenShotCanvas(it)
            draw(canvas)
        }
    }

    private fun canScrollToRight(): Boolean {
        return mOffsetValue < width
    }

    private fun canScrollToLeft(): Boolean {
        return mOffsetValue > 0
    }


    private fun initIdleState(w: Int) {
        val initOffset: Float = if (mIdleState == SUMMER_STATE) w.toFloat() else 0f
        updateOffset(initOffset)
        if (mIdleState == WINTER_STATE) {
            applyWinter()
        } else {
            applySummer()
        }

    }

    private fun applySummer() {

        applySummerImageBackground()
        applySummerTitleTV()
        applySummerProfile()
        applySummerIcon()
    }

    private fun applySummerProfile() {

        mProfileCircleIV?.load(R.drawable.john_snow_200_200) {
            allowHardware(false)
            transformations(CircleCropTransformation())
        }
    }

    private fun applySummerTitleTV() {

        mTitleTV?.let {

            it.setBackgroundResource(R.drawable.summer_text_background)
            it.setTextColor(Color.BLACK)
            it.text = "WINTER IS COMING..."
            // fix bug: text view with wrap_content layout param
            // does not resize its width and
            // last word of new text will not displayed if new string is longer
            it.post { it.requestLayout() }
        }

    }

    private fun applySummerImageBackground() {
        mBackgroundImg?.load(R.drawable.summer_550_309) {
            allowHardware(false)
        }

    }

    private fun applyWinter() {

        applyWinterImageBackground()
        applyWinterTitleTV()
        applyWinterProfile()
        applyWinterIcon()

    }

    private fun applyWinterProfile() {
        mProfileCircleIV?.load(R.drawable.night_king_200x200) {
            allowHardware(false)
            transformations(CircleCropTransformation())
        }
    }

    private fun applyWinterTitleTV() {
        mTitleTV?.let {

            it.setBackgroundResource(R.drawable.winter_text_background)
            it.setTextColor(Color.WHITE)
            it.text = "WINTER IS HERE..."
            // fix bug: text view with wrap_content layout param
            // does not resize its width and
            // last word of new text will not displayed if new string is longer
            it.post { it.requestLayout() }
        }
    }

    private fun applyWinterImageBackground() {
        mBackgroundImg?.load(R.drawable.winter_550_309) {
            allowHardware(false)
        }

    }

    private fun updateOffset(newOffsetValue: Float) {
        if (mOffsetValue == newOffsetValue) return
        mOffsetValue = newOffsetValue

        updateWeatherIconView()
    }

    private fun updateWeatherIconView() {

        mWeatherIcon?.let {

            val oldTranslatePosition = it.translationX

            Log.e("here","oldTranslatePosition: $oldTranslatePosition")
            val newTranslatePosition: Float
            val newAlpha: Float
            if (mOffsetValue < width / 2f) {
                newTranslatePosition = mOffsetValue
                if (oldTranslatePosition >= 0) {
                    applyWinterIcon()
                }
                val alphaPass = width / 2f
                newAlpha = 1 - newTranslatePosition / alphaPass
            } else {
                newTranslatePosition = mOffsetValue - width
                if (oldTranslatePosition <= 0) {
                    applySummerIcon()
                }
                val alphaPass = width / 2f
                newAlpha = 1 + newTranslatePosition / alphaPass
                it.alpha = newAlpha
            }
            it.alpha = newAlpha
            it.translationX = newTranslatePosition
        }
    }


    private fun applySummerIcon() {
        mWeatherIcon?.load(R.drawable.ic_sun) {
            allowHardware(false)
        }
    }

    private fun applyWinterIcon() {
        mWeatherIcon?.load(R.drawable.ic_snow) {
            allowHardware(false)
        }
    }

    private fun isChildExcluded(child: View?): Boolean {
        if (child == null) return false
        return child.id == R.id.weatherIconIV
    }


    companion object {
        // idle layout state
        private const val SUMMER_STATE = 1
        private const val WINTER_STATE = 2


        /**
         * animation states
         */
        private const val FROM_LEFT_TO_RIGHT = 1
        private const val FROM_RIGHT_TO_LEFT = 2
        private const val IDLE_ANIMATION_STATE = 3

    }


    private class ScreenShotCanvas(bitmap: Bitmap) : Canvas(bitmap)


}