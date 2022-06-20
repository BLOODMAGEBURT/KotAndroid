package com.xu.kotandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil.setContentView
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityMainBinding
import com.xu.kotandroid.ui.PanActivity

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {


    private fun initListener() {
        binding.pan.setOnClickListener {
            Log.d("ss", "sdfsdf")
            openActivity<PanActivity>()
        }
    }

    override fun initData() {

    }

    override fun initView() {
        initListener()
    }


}