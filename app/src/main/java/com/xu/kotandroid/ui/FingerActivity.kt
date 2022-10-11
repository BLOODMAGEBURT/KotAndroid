package com.xu.kotandroid.ui


import androidx.biometric.BiometricManager.Authenticators.*
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.AuthenticationCallback
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.ActivityUtils
import com.drake.net.utils.TipUtils.toast
import com.tencent.mmkv.MMKV
import com.xiao.biometricmanagerlib.FingerManager
import com.xiao.biometricmanagerlib.callback.SimpleFingerCallback
import com.xu.kotandroid.MainActivity
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.base.Const
import com.xu.kotandroid.databinding.ActivityFingerBinding
import java.util.concurrent.Executor

class FingerActivity : BaseActivity<ActivityFingerBinding>(R.layout.activity_finger) {


    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun initView() {

        binding.set.setOnClickListener {
            setFinger()
        }
        binding.verify.setOnClickListener {
            showFinger()
        }
        binding.device.setOnClickListener {
            showDevice()
        }

    }

    private fun showDevice() {
        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor, object : AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                toast("error:${errorCode}-${errString}")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                toast("failed")
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("解锁")
            .setSubtitle("请输入屏幕解锁密码")
            // Can't call setNegativeButtonText() and setAllowedAuthenticators() at the same time.
            .setAllowedAuthenticators(  DEVICE_CREDENTIAL)
            // .setNegativeButtonText("Use account password")
//            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }


    private fun setFinger() {
        when (FingerManager.checkSupport(this)) {

            FingerManager.SupportResult.DEVICE_UNSUPPORTED -> toast("您的设备不支持指纹")
            FingerManager.SupportResult.SUPPORT_WITHOUT_DATA -> {
                toast("请在系统中录入指纹后设置")
            }
            FingerManager.SupportResult.SUPPORT -> {
                FingerManager.build().setApplication(application).setTitle("指纹验证").setDes("请按下指纹")
                    .setNegativeText("取消")
                    //.setFingerDialogApi23(new MyFingerDialog())
                    // 如果你需要自定义android P 以下系统弹窗就设置,注意需要继承BaseFingerDialog，不设置会使用默认弹窗
                    .setFingerCallback(object : SimpleFingerCallback() {
                        override fun onSucceed() {
                            toast("验证成功")
                            ActivityUtils.startActivity(MainActivity::class.java)
                        }

                        override fun onFailed() {
                            toast("指纹无法识别")
                        }

                        override fun onChange() {
                            toast("指纹数据发生了变化")
                        }
                    }).create().startListener(this)
            }
            else -> toast("")
        }
    }

    private fun showFinger() {

        when (FingerManager.checkSupport(this)) {

            FingerManager.SupportResult.DEVICE_UNSUPPORTED -> toast("您的设备不支持指纹")
            FingerManager.SupportResult.SUPPORT_WITHOUT_DATA -> toast("指纹已关闭，请录入指纹")
            FingerManager.SupportResult.SUPPORT -> {
                verifyFinger()
            }
            else -> toast("")
        }
    }

    private fun verifyFinger() {
        if (FingerManager.hasFingerprintChang(this)) {
            MMKV.defaultMMKV()?.encode(Const.FINGER_LOGIN, false)
            FingerManager.updateFingerData(this)
            MaterialDialog(this).show {
                message(text = "系统指纹已变更，为了您的账户安全，请使用其它方式登录后重新开通")
                positiveButton(text = "确定") { dismiss() }
            }

        } else {
            FingerManager.build().setApplication(application).setTitle("指纹验证").setDes("请按下指纹")
                .setNegativeText("取消")
                //.setFingerDialogApi23(new MyFingerDialog())
                // 如果你需要自定义android P 以下系统弹窗就设置,注意需要继承BaseFingerDialog，不设置会使用默认弹窗
                .setFingerCallback(object : SimpleFingerCallback() {
                    override fun onSucceed() {
                        val email = MMKV.defaultMMKV()!!.decodeString(Const.USER_NAME).toString()
                        val pwd = MMKV.defaultMMKV()!!.decodeString(Const.PWD).toString()
                        // login with net

                    }

                    override fun onFailed() {
                        toast("指纹无法识别")
                    }

                    override fun onChange() {
                        toast("指纹数据发生了变化")
                        MMKV.defaultMMKV()?.encode(Const.FINGER_LOGIN, false)
                        FingerManager.updateFingerData(this@FingerActivity)
                        MaterialDialog(this@FingerActivity).show {
                            message(text = "系统指纹已变更，请重新密码登录")
                            positiveButton(text = "确定") { dismiss() }
                        }
                    }
                }).create().startListener(this)
        }

    }

    override fun initData() {

    }
}