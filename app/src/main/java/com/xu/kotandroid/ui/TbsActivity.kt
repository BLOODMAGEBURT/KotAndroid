package com.xu.kotandroid.ui


import android.os.Bundle
import android.util.Log
import com.drake.net.Get
import com.drake.net.utils.scopeDialog
import com.drake.net.utils.scopeNetLife
import com.hjq.permissions.Permission
import com.tencent.tbs.reader.ITbsReader
import com.tencent.tbs.reader.ITbsReaderCallback
import com.tencent.tbs.reader.TbsFileInterfaceImpl
import com.xu.kotandroid.R
import com.xu.kotandroid.aop.Permissions
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityTbsBinding
import java.io.File


class TbsActivity : BaseActivity<ActivityTbsBinding>(R.layout.activity_tbs) {

    //设置回调
    var callback =
        ITbsReaderCallback { actionType, args, result ->
            Log.i("TAG", "actionType=$actionType，args=$args，result=$result")
            if (ITbsReader.OPEN_FILEREADER_STATUS_UI_CALLBACK == actionType) {
                if (args is Bundle) {
                    val id = args.getInt("typeId")
                    if (ITbsReader.TBS_READER_TYPE_STATUS_UI_SHUTDOWN == id) {
                        TbsFileInterfaceImpl.getInstance().closeFileReader() //关闭fileReader
                    }
                }
            }
        }


    override fun initData() {
    }

    override fun initView() {
        binding.openFile.setOnClickListener {
            scopeDialog {
                val file =
                    Get<File>("https://mkdown-1256191338.cos.ap-beijing.myqcloud.com/aliyunecs.pdf") {
                        setDownloadDir(getExternalFilesDir("") ?: filesDir)
                    }.await()
                openFileReader(file.absolutePath)
            }

        }
    }

    @Permissions(
        Permission.READ_EXTERNAL_STORAGE,
        Permission.WRITE_EXTERNAL_STORAGE,
        Permission.WRITE_SETTINGS
    )
    private fun openFileReader(filePath: String) {
        //设置参数
        val extName = "pdf"
        val param = Bundle()
        param.putString("filePath", filePath) //文件路径
        param.putString("fileExt", extName) //文件后缀名
        param.putString("tempPath", getExternalFilesDir("temp")!!.absolutePath)


        //调用openFileReader打开文档
        if (TbsFileInterfaceImpl.canOpenFileExt(extName)) { //tbs支持的文档类型
            val ret = TbsFileInterfaceImpl.getInstance().openFileReader(this, param, callback, null)
            if (ret != 0) {
                Log.e("here", "openFileReader失败, ret = $ret")
            }
            ret
        } else {
            //tbs不支持的文档类型
            //...
            -1
        }

    }


}