package com.xu.kotandroid.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView

/**
 * @Author Xu
 * Date：2021/9/24 4:54 下午
 * Description：
 */
class MyTextView : AppCompatTextView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> Log.e("xu", "textView dispatch")

        }

        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> Log.e("xu", "textView: touch")

        }

        return false
    }

    override fun hasOnClickListeners(): Boolean {
        Log.e("xu", "textView onclick")
        return super.hasOnClickListeners()
    }

}