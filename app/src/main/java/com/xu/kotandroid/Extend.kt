package com.xu.kotandroid

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