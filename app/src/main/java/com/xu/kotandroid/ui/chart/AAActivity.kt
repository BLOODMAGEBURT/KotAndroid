package com.xu.kotandroid.ui.chart

import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aachartcreator.aa_toAAOptions
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityAaactivityBinding

class AAActivity : BaseActivity<ActivityAaactivityBinding>(R.layout.activity_aaactivity) {


    override fun initView() {


    }

    override fun initData() {
        binding.aaChartView.aa_drawChartWithChartModel(configurePieChart())
    }


    private fun configurePieChart(): AAChartModel {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Pie)
            .backgroundColor("#ffffff")
//            .title("LANGUAGE MARKET SHARES JANUARY,2020 TO MAY")
//            .subtitle("virtual data")
            .colorsTheme(arrayOf("#0c9674", "#7dffc0", "#d11b5f", "#facd32", "#ffffa0"))
            .dataLabelsEnabled(true)//是否直接显示扇形图数据
            .legendEnabled(false)
            .tooltipEnabled(false)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Language market shares")
//                        .size("70%")
                        .innerSize("70%")
                        .data(
                            arrayOf(
//                                arrayOf("Java", 67),
//                                arrayOf("Swift", 999),
//                                arrayOf("Python", 83),
//                                arrayOf("OC", 11),
//                                arrayOf("Go", 30)
                                67,
                                999,
                                83,
                                11,
                                30
                            )
                        )
                )
            )

        val aaOptions = aaChartModel.aa_toAAOptions()
        aaOptions.plotOptions?.series?.dataLabels?.format =
            "{point.percentage:.1f} %" //保留 Y 轴值的小数点后 4 位


        return aaChartModel
    }

}