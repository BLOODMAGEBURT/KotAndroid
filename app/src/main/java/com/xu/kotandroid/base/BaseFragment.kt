package com.xu.kotandroid.base

import android.database.DatabaseUtils
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 *  author : xujianbo
 *  date : 2021/9/8 11:10 下午
 *  description :
 */
abstract class BaseFragment<B : ViewDataBinding>(@LayoutRes layoutId: Int = 0) :
    Fragment(layoutId) {
    lateinit var binding: B


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.bind(view)!!

        try {
            initView()
            initData()
        } catch (e: Exception) {
            Log.e("日志", "初始化失败")
            e.printStackTrace()
        }
    }

    abstract fun initView()
    abstract fun initData()

}