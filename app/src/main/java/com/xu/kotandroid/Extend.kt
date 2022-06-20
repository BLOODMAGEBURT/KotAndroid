package com.xu.kotandroid

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources

/**
 * @Author Xu
 * Date：2021/11/9 3:17 下午
 * Description：
 */
val Int.dp: Float
    get() = (this / Resources.getSystem().displayMetrics.density)

val Int.px: Float
    get() = (this * Resources.getSystem().displayMetrics.density)


internal inline fun <reified T : Activity> Context.openActivity(block: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    if (this !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.block()
    startActivity(intent)
}