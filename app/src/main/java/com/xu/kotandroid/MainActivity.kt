package com.xu.kotandroid


import android.content.Intent
import android.net.Uri
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityMainBinding
import com.xu.kotandroid.ui.*


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {


    private fun initListener() {
        binding.pan.setOnClickListener {
            openActivity<PanActivity>()
        }

        binding.add.setOnClickListener {
            MoreMenuPopupWindow(this).showPopupWindow(binding.add, 300, 200)
        }

        binding.toCalendar.setOnClickListener {

            openActivity<CalenderActivity>()
        }

        binding.toFinger.setOnClickListener { openActivity<FingerActivity>() }

        binding.toComment.setOnClickListener {
//            comment()
            openActivity<WebActivity>()
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
    }


}