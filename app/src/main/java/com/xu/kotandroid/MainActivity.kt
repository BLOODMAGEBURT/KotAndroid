package com.xu.kotandroid

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityMainBinding
import com.xu.kotandroid.ui.CalenderActivity
import com.xu.kotandroid.ui.MoreMenuPopupWindow
import com.xu.kotandroid.ui.PanActivity
import com.xu.kotandroid.util.CalendarEventsUtils
import java.util.*


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
    }



    override fun initData() {

    }

    override fun initView() {
        initListener()
    }


}