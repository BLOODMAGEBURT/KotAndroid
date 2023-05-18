package com.xu.kotandroid.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
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

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")

                binding.iv.load(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }


    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            if (uris.isNotEmpty()) {
                Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }


    override fun initData() {

    }

    override fun initView() {
        binding.tvChoose.setOnClickListener {
            resultLauncher.launch(true)
        }

        binding.choosePic.setOnClickListener {

            if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(this)) {
                Log.d("PhotoPicker", "Preview is supported")

                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                Log.d("PhotoPicker", "Preview is not supported")
                Toast.makeText(this, "不支持图片选择器", Toast.LENGTH_SHORT).show()
            }
        }

        binding.chooseMultiPic.setOnClickListener {
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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