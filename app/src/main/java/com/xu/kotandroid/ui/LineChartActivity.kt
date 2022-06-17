package com.xu.kotandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xu.kotandroid.R
import com.xu.kotandroid.bean.FenShiBean
import com.xu.kotandroid.const.Const
import com.xu.kotandroid.util.JsonUtils
import com.xu.kotandroid.view.MiniFenShiView

class LineChartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_chart)


        initView()
    }

    private fun initView() {

        val fsView = findViewById<MiniFenShiView>(R.id.fsView)
        val data = JsonUtils.getData(this, "fenshi.json", FenShiBean::class.java)

        fsView.setNewData(data)
        fsView.resetCurrentMode("down")
    }
}