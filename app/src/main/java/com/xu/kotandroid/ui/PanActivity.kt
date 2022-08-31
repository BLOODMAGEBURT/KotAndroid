package com.xu.kotandroid.ui

import android.text.InputFilter
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityPanBinding
import com.xu.kotandroid.util.PriceInputFilter2

class PanActivity : BaseActivity<ActivityPanBinding>(R.layout.activity_pan) {


    override fun initView() {

        binding.et.filters = arrayOf<InputFilter>(PriceInputFilter2(4, 2,1,4))

    }

    override fun initData() {

    }
}