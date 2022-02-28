package com.xu.kotandroid.const

/**
 * @Author Xu
 * Date：2021/11/11 4:01 下午
 * Description：
 */
sealed class TimeUnit(interval: Int) {

    data class A(val interval1: Int) : TimeUnit(interval1)


}
