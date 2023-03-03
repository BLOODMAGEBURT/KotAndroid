package com.xu.kotandroid

import android.app.Application
import android.app.ProgressDialog
import com.drake.brv.utils.BRV
import com.drake.net.NetConfig
import com.drake.net.interceptor.LogRecordInterceptor
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDialogFactory
import com.drake.net.okhttp.setLog
import com.drake.net.okhttp.setRequestInterceptor
import com.drake.net.request.BaseRequest
import com.tencent.mmkv.MMKV
import com.tencent.tbs.reader.TbsFileInterfaceImpl
import com.xu.kotandroid.base.Const
import com.xu.kotandroid.converter.GsonConverter
import java.util.concurrent.TimeUnit

/**
 * @Author Xu
 * Date：2021/9/23 5:36 下午
 * Description：
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        // 初始化BindingAdapter的默认绑定ID
        BRV.modelId = BR.m
        initNet()
        initTbs()
    }

    private fun initTbs() {
        TbsFileInterfaceImpl.setLicenseKey(Const.TBS_KEY)
        TbsFileInterfaceImpl.initEngine(this)
    }

    private fun initNet() {


        NetConfig.init("http://43.128.31.195/") {

            // 超时设置
            connectTimeout(2, TimeUnit.MINUTES)
            readTimeout(2, TimeUnit.MINUTES)
            writeTimeout(2, TimeUnit.MINUTES)

            setLog(BuildConfig.DEBUG) // LogCat异常日志
            addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG)) // 添加日志记录器

            setRequestInterceptor(object : RequestInterceptor { // 添加请求拦截器
                override fun interceptor(request: BaseRequest) {
                    request.addHeader("client", "Net")
                    request.setHeader("token", "123456")
                }
            })

            setConverter(GsonConverter()) // 数据转换器

            setDialogFactory { // 全局加载对话框
                ProgressDialog(it).apply {
                    setMessage("我是全局自定义的加载对话框...")
                }
            }
        }


    }


}