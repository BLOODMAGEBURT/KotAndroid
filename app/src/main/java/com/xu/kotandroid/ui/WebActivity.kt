package com.xu.kotandroid.ui

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.databinding.DataBindingUtil.setContentView
import com.blankj.utilcode.util.ActivityUtils
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityWebBinding

class WebActivity : BaseActivity<ActivityWebBinding>(R.layout.activity_web) {
    override fun initData() {
//        binding.wv.loadUrl("https://juejin.cn/post/6844904020650229774")
        binding.wv.loadUrl("http://192.168.230.251:8020/?hq_mode=rt&code=09626&name=%E5%93%94%E5%93%A9%E5%93%94%E5%93%A9%EF%BC%8D%EF%BC%B7&theme=dark&type=hk")
    }

    override fun initView() {
        initWebSetting()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebSetting() {
        val webSettings = binding.wv.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
//        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        binding.wv.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {

            }

            /**
             * 此 API 可处理 WebView 对象的渲染器进程消失（可能是因为系统为收回急需的内存终止了渲染器，
             * 或是因为渲染器进程本身已崩溃）的情况。通过使用此 API，即使渲染器进程已经消失，
             * 您的应用也可以继续执行。
             */
            override fun onRenderProcessGone(
                view: WebView?,
                detail: RenderProcessGoneDetail?
            ): Boolean {
                binding.wv.reload()
                return true
            }
        }
        binding.wv.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {

            }
        }
    }

    override fun onDestroy() {
        binding.wv.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        binding.wv.settings.javaScriptEnabled = false
        binding.wv.clearHistory()
        binding.wv.removeAllViews()
        binding.cl.removeView(binding.wv)
        binding.wv.destroy()

        super.onDestroy()
    }

}