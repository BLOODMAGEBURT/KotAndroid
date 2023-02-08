package com.xu.kotandroid.util.anim

import android.animation.TypeEvaluator

/**
 * @Author Xu
 * Date：2023/2/8 11:31
 * Description：
 */
class CharEvaluator : TypeEvaluator<Char> {
    override fun evaluate(fraction: Float, startValue: Char, endValue: Char): Char {

        /**
         *  ASCII码表
         *  'A' -> 65
         *  'Z' -> 90
         */
        val start = startValue.code

        return (start + (endValue.code - start) * fraction).toInt().toChar()
    }
}