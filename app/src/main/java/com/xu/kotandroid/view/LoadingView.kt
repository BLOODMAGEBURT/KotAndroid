package com.xu.kotandroid.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.animation.addListener
import androidx.core.animation.doOnRepeat
import com.blankj.utilcode.util.AdaptScreenUtils
import com.xu.kotandroid.R

/**
 * @Author Xu
 * Date：2023/2/8 09:43
 * Description：
 */
class LoadingView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    AppCompatImageView(context, attributeSet, defStyleAttr) {

    private var mTop = 0


    private val pt100 by lazy {
        AdaptScreenUtils.pt2Px(100f)
    }

    private val images = listOf(R.drawable.add_nor, R.drawable.ai_toggle_bg)

    init {

        setImageResource(R.drawable.add_nor)
        initAnim()


    }

    private fun initAnim() {
        ValueAnimator.ofInt(0, pt100, 0).apply {

            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                val dy = (it.animatedValue as Int)
                /**
                 * Sets the top position of this view relative to its parent
                 */
                top = mTop - dy
            }
            var count = 0
            doOnRepeat {
                count++
                toggleImage(count)
            }
            start()
        }
    }

    private fun toggleImage(count: Int) {
        setImageResource(images[count % 2])
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mTop = top
        Log.e("top", "top: $top")
    }


}