package com.xu.kotandroid.view

import com.xu.kotandroid.bean.FenShiBean

/**
 * @Author yangsanning
 * @ClassName Extremum
 * @Description 极值(最大值 、 最小值 、 极差)
 * @Date 2020/7/9
 */
class Extremum {
    /**
     * 最大值
     */
    var maximum = 0.0

    /**
     * 最小值
     */
    var minimum = 0.0

    /**
     * 极差
     */
    private var peek = 0.0

    /**
     * 初始化最大值以及最小值
     *
     * @param value 初始值
     */
    fun init(value: Double) {
        minimum = value
        maximum = minimum
    }

    /**
     * 计算极差
     */
    fun calculatePeek() {
        peek = maximum - minimum
    }

    /**
     * 重置极值(最大值 、 最小值 、 极差)
     */
    fun reset() {
        peek = 0.0
        minimum = peek
        maximum = minimum
    }

    /**
     * 进行最大值和最小值比较
     */
    fun convert(trends: List<FenShiBean.Trend>) {
        maximum = trends.maxByOrNull { it.price }!!.price
        minimum = trends.minByOrNull { it.price }!!.price
    }
}