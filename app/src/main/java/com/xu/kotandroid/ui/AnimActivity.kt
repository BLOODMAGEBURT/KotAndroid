package com.xu.kotandroid.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityAnimBinding
import com.xu.kotandroid.util.anim.CharEvaluator

class AnimActivity : BaseActivity<ActivityAnimBinding>(R.layout.activity_anim) {


    private var textAnim: ValueAnimator? = null
    private var objAnim: ObjectAnimator? = null


    override fun initView() {

        binding.animTv.setOnClickListener { doTextAnim() }
        binding.objectTv.setOnClickListener { doObjAnim() }
        binding.animSet.setOnClickListener { doSetAnim() }


    }

    private fun doSetAnim() {

        if (textAnim?.isRunning == true) {
            return
        }

        if (objAnim?.isRunning == true) {
            return
        }


        AnimatorSet().apply {
            // 会覆盖各自的duration,使用统一的duration
            duration = 3000
            playSequentially(textAnim, objAnim)
            start()
        }

    }

    private fun doObjAnim() {
        /**
         * alpha
         * rotation rotationX rotationY
         * scaleX scaleY
         * translationX translationY
         */
        if (objAnim?.isRunning == true) {
            return
        }
        objAnim =
            ObjectAnimator.ofFloat(binding.objectTv, "translationX", 0f, 180f, -180f, 0f).apply {
                duration = 1500
                start()
            }
    }


    private fun doTextAnim() {

        if (textAnim?.isRunning == true) {
            return
        }

        textAnim = ValueAnimator.ofObject(CharEvaluator(), 'A', 'Z').apply {

            duration = 8000
            interpolator = LinearInterpolator()

            addUpdateListener {
                binding.animTv.text = (it.animatedValue as Char).toString()
            }

            start()
        }


    }


    override fun initData() {

        binding.animTv.text = "A"

    }

    override fun onDestroy() {
        super.onDestroy()
        textAnim?.cancel()
        objAnim?.cancel()
    }

}