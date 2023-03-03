package com.xu.kotandroid.ui


import android.widget.Toast
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.hjq.permissions.Permission
import com.xu.kotandroid.R
import com.xu.kotandroid.aop.Permissions
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityAopBinding
import com.xu.kotandroid.ui.model.TextModel

class AopActivity : BaseActivity<ActivityAopBinding>(R.layout.activity_aop) {


    override fun initView() {
        binding.rv.linear().setup {
            addType<TextModel>(R.layout.item_text)
            R.id.itemRoot.onClick {
                handelClick(getModel<TextModel>().text)
            }
        }.models = makeData()
    }

    private fun makeData(): List<Any> {
        return listOf(
            TextModel("Permission"),
        )
    }

    private fun handelClick(text: String) {
        when (text) {
            "Permission" -> {
                takePhoto()
            }
            else -> {}
        }
    }

    @Permissions(Permission.CAMERA)
    private fun takePhoto() {
        Toast.makeText(this, "摄像头权限获取成功", Toast.LENGTH_SHORT).show()
    }


    override fun initData() {
    }
}