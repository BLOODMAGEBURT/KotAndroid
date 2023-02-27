package com.xu.kotandroid.ui

import android.graphics.Color
import android.text.InputFilter
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.drake.net.Get
import com.drake.net.utils.fastest
import com.drake.net.utils.scopeNetLife
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityPanBinding
import com.xu.kotandroid.util.PriceInputFilter2
import kotlinx.coroutines.*

class PanActivity : BaseActivity<ActivityPanBinding>(R.layout.activity_pan) {


    override fun initView() {

    }

    override fun initData() {

        val pie = AnyChart.pie().apply {
            data(
                listOf(
                    ValueDataEntry("john", 10000),
                    ValueDataEntry("john", 12000),
                    ValueDataEntry("john", 18000)
                )
            )
            labels().position("outside")
        }
        binding.pie.setChart(pie)


        binding.donut.apply {

            donutColors = intArrayOf(
                Color.parseColor("#FFFFFF"),
                Color.parseColor("#9EFFFFFF"),
                Color.parseColor("#8DFFFFFF")
            )

            animation.duration = 1000L
            animate(
                listOf(
                    20f,
                    80f,
                    100f
                )
            )

        }


    }

}