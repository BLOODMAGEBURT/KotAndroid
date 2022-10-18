package com.xu.kotandroid.ui

import android.text.InputFilter
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

        binding.et.filters = arrayOf<InputFilter>(PriceInputFilter2(4, 2, 1, 4))


    }

    override fun initData() {

        runBlocking {

        }

        scopeNetLife {

            delay(200L)

            launch {
                yield()
            }

            val deffered = Get<String>("")

            withTimeout(200) {}
            fastest(listOf(deffered))
        }

    }

}