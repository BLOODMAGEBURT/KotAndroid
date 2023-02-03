package com.xu.kotandroid.ui.chart

import com.antgroup.antv.f2.F2CanvasView
import com.antgroup.antv.f2.F2CanvasView.Adapter
import com.antgroup.antv.f2.F2Chart
import com.antgroup.antv.f2.F2Chart.AxisConfigBuilder
import com.antgroup.antv.f2.F2Chart.CoordConfigBuilder
import com.antgroup.antv.f2.F2Config
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityF2Binding
import com.xu.kotandroid.util.Utils


class F2Activity : BaseActivity<ActivityF2Binding>(R.layout.activity_f2) {


    override fun initView() {

        val mCanvasView = binding.canvasView
        mCanvasView.initCanvasContext()

        var mChart: F2Chart? = null

        mCanvasView.setAdapter(object : Adapter {
            override fun onCanvasDraw(p0: F2CanvasView?) {

                if (mChart == null) {
                    mChart = F2Chart.create(
                        mCanvasView.context,
                        "SingleIntervalChart_2",
                        mCanvasView.width.toDouble(),
                        mCanvasView.height.toDouble()
                    )
                }

                mChart!!.setCanvas(mCanvasView)
                mChart!!.padding(10.0, 20.0, 0.0, 10.0)
                mChart!!.source(
                    Utils.loadAssetFile(
                        mCanvasView.context,
                        "mockData_basePie.json"
                    )
                )
                mChart!!.setAxis(
                    "percent", AxisConfigBuilder()
                        .lineHidden().labelHidden().gridHidden()
                )
                mChart!!.setAxis(
                    "a", AxisConfigBuilder()
                        .labelHidden().lineHidden().gridHidden()
                )
                mChart!!.setCoord(CoordConfigBuilder().type("polar").transposed(true))
                mChart!!.interval().position("a*percent")
                    .style(
                        F2Config.Builder<F2Config.Builder<*>>().setOption("lineWidth", 3).build()
                    ).color("name")
                mChart?.render()
            }

            override fun onDestroy() {
                mChart?.destroy()
            }
        })


    }


    override fun initData() {

    }

}