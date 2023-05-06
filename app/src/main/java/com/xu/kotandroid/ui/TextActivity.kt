package com.xu.kotandroid.ui

import android.graphics.Color
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityTextBinding

class TextActivity : BaseActivity<ActivityTextBinding>(R.layout.activity_text) {

    private val donutSet = listOf(
        13f,
        1f,
        6.6f,
        1f,
        (100 - 13 - 6.6 - 3).toFloat(),
        1f,
    )
    private val animationDuration = 1000L
    override fun initView() {

        binding.donutChart.apply {

            donutColors = intArrayOf(
                Color.parseColor("#478DFF"),
                Color.parseColor("#191919"),
                Color.parseColor("#34C759"),
                Color.parseColor("#191919"),
                Color.parseColor("#383838"),
                Color.parseColor("#191919"),
            )

            animation.duration = animationDuration
            animate(donutSet)
        }
    }

    override fun initData() {

    }

}