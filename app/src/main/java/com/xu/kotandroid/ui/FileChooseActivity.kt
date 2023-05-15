package com.xu.kotandroid.ui

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityFileChooseBinding

class FileChooseActivity : BaseActivity<ActivityFileChooseBinding>(R.layout.activity_file_choose) {


    @SuppressLint("SetTextI18n")
    private val resultLauncher = registerForActivityResult(ResultContract()) {
        it?.data?.let { uri ->

            binding.tvFilePath.text = "$uri  type: ${contentResolver.getType(uri)}"
        }
    }


    override fun initData() {


    }

    override fun initView() {
        binding.tvChoose.setOnClickListener {
            resultLauncher.launch(true)
        }


    }


    class ResultContract : ActivityResultContract<Boolean, Intent?>() {


        override fun createIntent(context: Context, input: Boolean): Intent {
            return Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }
        }


        override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
            return intent
        }
    }


}