package com.xu.kotandroid.adapter

import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.xu.kotandroid.R

/**
 * @Author Xu
 * Date：2023/4/4 10:32
 * Description：
 */
object CustomBindingAdapter {

    @JvmStatic
    @BindingAdapter("isActive")
    fun isActive(tv: TextView, isActive: Boolean) {
        if (isActive) {
            tv.background =
                AppCompatResources.getDrawable(tv.context, R.drawable.text_blue_corner16_bg)
            tv.setTextColor(tv.context.getColor(R.color.btn_text_white))
        } else {
            tv.background =
                AppCompatResources.getDrawable(tv.context, R.drawable.text_27_corner16_bg)
            tv.setTextColor(tv.context.getColor(R.color.white))
        }
    }

    /**
     * 隐藏控件
     * @param isGone 当为true则显示[View.VISIBLE], 否则隐藏[View.GONE]
     */
    @BindingAdapter("gone")
    @JvmStatic
    fun setVisibleOrGone(v: View, isGone: Boolean) {
        v.visibility = if (isGone) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


}