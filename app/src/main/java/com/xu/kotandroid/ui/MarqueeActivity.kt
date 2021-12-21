package com.xu.kotandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.gongwen.marqueen.SimpleMF
import com.xu.kotandroid.R
import com.xu.kotandroid.databinding.ActivityMarqueeBinding


class MarqueeActivity : AppCompatActivity() {

    lateinit var binding: ActivityMarqueeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_marquee)

        initData()
    }

    private fun initData() {
        val titles =
            listOf("1.《赋得古原草送别》", "2.离离原上草，一岁一枯荣。野火烧不尽，春风吹又生。远芳侵古道，晴翠接荒城。", "3.又送王孙去，萋萋满别情。")

        val factory = SimpleMF<String>(this).apply {
            data = titles
        }

        binding.simpleMarqueeView.apply {
            setMarqueeFactory(factory)
            startFlipping()
        }



        binding.mv4.setContent("1.《赋得古原草送别》")



    }

    override fun onPause() {
        super.onPause()
        binding.mv4.stopRoll()
    }

    override fun onResume() {
        super.onResume()
        binding.mv4.continueRoll()
    }
}