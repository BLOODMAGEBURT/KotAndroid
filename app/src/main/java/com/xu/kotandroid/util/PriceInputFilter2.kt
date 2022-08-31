package com.xu.kotandroid.util

import android.text.InputFilter
import android.text.Spanned
import java.util.*
import java.util.regex.Pattern

/**
 * @Author Xu
 * Date：2021/6/23 4:16 下午
 * Description：
 */
class PriceInputFilter2(
    digitsBeforeZero: Int,
    digitsAfterZero: Int,
    advanceBefore: Int = -1,
    advanceAfter: Int = -1
) : InputFilter {
    var mPattern: Pattern
    var pattern2: Pattern

    /**
     * source 新输入的字符串
     * start 新输入的字符串起始下标,一般为0
     * end 新输入的字符串终点下标,一般为source长度-1
     * dest 输入之前文本的内容
     * dstart 原内容起始坐标 一般为0
     * dend 原内容终点坐标,一般为dest长度-1
     *
     *
     * 链接：https://juejin.cn/post/6844904006418956295
     *
     *
     * 返回null，表示可正常添加source字符串；
     * 返回""，表示不变动原字符；
     * 返回以上之外的字符串，表示将返回的该字符串追加到原字符串中
     */
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        //直接输入"."返回"0."
        //".x"删除"x"输出为"."，inputFilter无法处理成"0."，所以只处理直接输入"."的case
        if ("." == source && "" == dest.toString()) {
            return "0."
        }
        // 直接输入0
        if ("0" == source && "" == dest.toString()) {
            return "0."
        }
        val builder = StringBuilder(dest)
        if ("" == source) {
            builder.replace(dstart, dend, "")
        } else {
            builder.insert(dstart, source)
        }
        val resultTemp = builder.toString()

        //判断修改后的数字是否满足小数格式，不满足则返回 "",不允许修改
        resultTemp.toDoubleOrNull()?.let {

            val matcher =
                if (it >= 1) mPattern.matcher(resultTemp) else pattern2.matcher(resultTemp)

            return if (!matcher.matches()) {
                ""
            } else null
        } ?: return null

    }

    /**
     * "[0-9]{0,digitsBeforeZero}+(\\.[0-9]{0,digitsAfterZero})?"
     * [0-9]{0,digitsBeforeZero}+  表示有0-digitsBeforeZero个0-9的整数；
     * (\.[0-9]{0,digitsAfterZero})?  表示有0-digitsAfterZero个0-9的小数，小数可有可无。
     * 也就是说，123，123.1，123.12都是符合条件的。
     */
    init {
        val regex = String.format(
            Locale.CHINA,
            "[0-9]{0,%d}+(\\.[0-9]{0,%d})?",
            digitsBeforeZero,
            digitsAfterZero
        )
        mPattern = Pattern.compile(regex)


        val regex2 = String.format(
            Locale.CHINA,
            "[0-9]{0,%d}+(\\.[0-9]{0,%d})?",
            advanceBefore,
            advanceAfter
        )

        pattern2 = Pattern.compile(regex2)

    }
}