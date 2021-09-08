package com.xu.kotandroid.base

import android.util.Log
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 *  author : xujianbo
 *  date : 2021/9/8 10:56 下午
 *  description :
 */
abstract class BaseActivity<B : ViewDataBinding>(@LayoutRes layoutId: Int = 0) :
    AppCompatActivity(layoutId) {

    lateinit var binding: B

    override fun setContentView(layoutResID: Int) {

        val rootView = layoutInflater.inflate(layoutResID, null)
        setContentView(rootView)
        binding = DataBindingUtil.bind(rootView)!!
        init()
    }

    private fun init() {
        try {
            initView()
            initData()
        } catch (e: Exception) {
            Log.e("日志", "初始化失败")
            e.printStackTrace()
        }
    }

    abstract fun initData()
    abstract fun initView()

}