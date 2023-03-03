package com.xu.kotandroid


import android.content.Intent
import android.net.Uri
import com.blankj.utilcode.util.DeviceUtils.getModel
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityMainBinding
import com.xu.kotandroid.ui.*
import com.xu.kotandroid.ui.chart.AAActivity
import com.xu.kotandroid.ui.chart.F2Activity
import com.xu.kotandroid.ui.chart.PieActivity
import com.xu.kotandroid.ui.model.TextModel
import org.w3c.dom.Text


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {


    private fun initListener() {


        binding.add.setOnClickListener {
            MoreMenuPopupWindow(this).showPopupWindow(binding.add, 300, 200)
        }
    }

    private fun comment() {
        try {
            val uri = Uri.parse(
                "market://details?id=cn.futu.trader"

            ) //需要评分的APP包名
            Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                setClassName("com.xiaomi.market", "com.xiaomi.market.ui.AppDetailActivity")
//                setPackage("com.heytap.market")
                startActivity(this)
            }

        } catch (e: Exception) {
        }
    }


    override fun initData() {

    }

    override fun initView() {
        initListener()

        binding.rv.linear().setup {
            addType<TextModel>(R.layout.item_text)
            R.id.itemRoot.onClick {
                handelClick(getModel<TextModel>().text)
            }
        }.models = makeData()

    }

    private fun handelClick(text: String) {
        when (text) {
            "toPan" -> openActivity<PanActivity>()
            "toCalendar" -> openActivity<CalenderActivity>()
            "toFinger" -> openActivity<FingerActivity>()
            "toComment" -> comment()
            "toBtm" -> openActivity<BtmActivity>()
            "toLimit" -> openActivity<LimitActivity>()
            "toPenny" -> openActivity<PennyActivity>()
            "toF2" -> openActivity<F2Activity>()
            "toAA" -> openActivity<AAActivity>()
            "toPie" -> openActivity<PieActivity>()
            "toLine" -> openActivity<LineChartActivity>()
            "toAnim" -> openActivity<AnimActivity>()
            "toText" -> openActivity<TextActivity>()
            "toAop" -> openActivity<AopActivity>()
            "toTbs" -> openActivity<TbsActivity>()
            else -> {}
        }
    }


    private fun makeData(): List<TextModel> {

        return listOf(
            TextModel("toPan"),
            TextModel("toCalendar"),
            TextModel("toFinger"),
            TextModel("toComment"),
            TextModel("toBtm"),
            TextModel("toLimit"),
            TextModel("toPenny"),
            TextModel("toF2"),
            TextModel("toAA"),
            TextModel("toPie"),
            TextModel("toLine"),
            TextModel("toAnim"),
            TextModel("toText"),
            TextModel("toAop"),
            TextModel("toTbs"),
        )
    }

}



