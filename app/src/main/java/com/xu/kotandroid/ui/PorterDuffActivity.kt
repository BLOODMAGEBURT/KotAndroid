package com.xu.kotandroid.ui

import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.blankj.utilcode.util.AdaptScreenUtils
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityPorterDuffBinding
import com.xu.kotandroid.view.ArrowDrawable

class PorterDuffActivity : BaseActivity<ActivityPorterDuffBinding>(R.layout.activity_porter_duff) {

    override fun initData() {
        (binding.iv.drawable as ArrowDrawable).setProgress(0)
    }

    override fun initView() {
        val pt200 = AdaptScreenUtils.pt2Px(200f)
        ArrowDrawable(pt200, pt200).apply {
            binding.iv.setImageDrawable(this)
        }


        binding.progress.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                (binding.iv.drawable as ArrowDrawable).setProgress(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


    }

}