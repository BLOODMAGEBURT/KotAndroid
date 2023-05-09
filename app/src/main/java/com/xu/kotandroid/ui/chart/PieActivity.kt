package com.xu.kotandroid.ui.chart

import android.graphics.Color
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityPieBinding
import com.xu.kotandroid.view.PieChartView2.PieceDataHolder


class PieActivity : BaseActivity<ActivityPieBinding>(R.layout.activity_pie) {


    override fun initView() {

    }


    override fun initData() {


        val pieceDataHolders: MutableList<PieceDataHolder> = ArrayList()

        pieceDataHolders.add(PieceDataHolder(10000f, Color.RED, "小京，５"))
//        pieceDataHolders.add(PieceDataHolder(100f, -0x883356, "今天，１"))
//        pieceDataHolders.add(PieceDataHolder(1000f, -0xee55cd, "明天，２"))
//        pieceDataHolders.add(PieceDataHolder(1200f, Color.GRAY, "风和，３"))
        pieceDataHolders.add(PieceDataHolder(5000f, Color.YELLOW, "呵呵，４"))
        pieceDataHolders.add(PieceDataHolder(13000f, Color.BLUE, "花花，６"))

        binding.pie.apply {
            useInnerCircle = true
            innerCirclePercent = 0.7f
            setData(pieceDataHolders)
        }

    }

}