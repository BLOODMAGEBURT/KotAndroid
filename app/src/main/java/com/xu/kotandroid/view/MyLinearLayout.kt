package com.xu.kotandroid.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout

/**
 * @Author Xu
 * Date：2021/9/24 4:49 下午
 * Description：
 */
class MyLinearLayout : LinearLayout {

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    )


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> Log.e("xu", "Linear dispatch")

        }

        return super.dispatchTouchEvent(ev)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> Log.e("xu", "Linear intercept")

        }

        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            Log.e("xu", "Linear touch")
        }

        return false
    }

}