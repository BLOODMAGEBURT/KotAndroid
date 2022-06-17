package com.xu.kotandroid.view

import androidx.annotation.IntRange
import com.xu.kotandroid.bean.FenShiBean.Trend
import com.xu.kotandroid.bean.FenShiBean
import com.xu.kotandroid.bean.ViewPoint

/**
 * @Author yangsanning
 * @ClassName MiniFenShiDataManager
 * @Description 迷你分时数据管理
 * @Date 2020/6/19
 */
class MiniFenShiDataManager(
    /**
     * 分时参数配置
     */
    private val config: MiniFenShiConfig
) {
    /**
     * priceList: 价格集合
     * lastClose: 收盘价
     * maxStockPrice: 最大价格
     * minStockPrice: 最小价格
     */
    private var fenShiDataList: List<Trend>? = null
    val points = mutableListOf<ViewPoint>()

    var preClose = 0.0
    var preCloseY = 0f
    var extremum = Extremum()

    /**
     * 数据总数(即表格需要绘制的点总数)
     */
    var totalCount = 0

    /**
     * 价格是否为空
     */
    val isPriceEmpty: Boolean
        get() = fenShiDataList!!.isEmpty()

    /**
     * 根据position获取价格
     */
    fun getPrice(@IntRange(from = 0) position: Int): Double {
        return if (position < priceSize()) fenShiDataList!![position].price else 0.0
    }

    /**
     * 获取最后一个价格
     */
    val lastPrice: Double
        get() = getPrice(lastPricePosition)

    /**
     * 价格集合大小
     */
    fun priceSize(): Int {
        return fenShiDataList!!.size
    }

    /**
     * 获取最后一个价格position
     */
    val lastPricePosition: Int
        get() = priceSize() - 1

    /**
     * 设置数据
     */
    fun setNewData(fenShi: FenShiBean) {
        // 重置数据
        preClose = 0.0
        fenShiDataList = fenShi.trends
        if (fenShiDataList!!.isNotEmpty()) {

            extremum.init(fenShiDataList!![0].price)
            extremum.convert(fenShiDataList!!)

            preClose = fenShi.preClose
            totalCount = fenShi.trends.size

            // 重置最值
            resetPeakPrice()

            // 重置当前颜色
            resetCurrentColor()
        }
    }

    /**
     * 计算坐标点
     */
    fun calculatePointList(width: Int, height: Int, fenShi: FenShiBean) {

        val xPeriod = (width / 65f)

        val trends = fenShi.trends

        preCloseY =
            ((fenShi.preClose - extremum.minimum) / (extremum.maximum - extremum.minimum) * height).toFloat()

        trends.forEachIndexed { index, it ->
            // 第一条小于当天12点
            points.add(
                ViewPoint(
                    xPeriod * index,
                    ((it.price - extremum.minimum) / (extremum.maximum - extremum.minimum) * height).toFloat()
                )
            )
        }

    }

    /**
     * 重置最值
     */
    private fun resetPeakPrice() {
        if (extremum.maximum == extremum.minimum) {
            extremum.minimum = extremum.maximum / 2f
            extremum.maximum = extremum.maximum * 3f / 2f
        }
        if (config.alwaysShowDottedLine) {
            if (preClose > extremum.maximum) {
                extremum.maximum = preClose
            } else if (preClose < extremum.minimum) {
                extremum.minimum = preClose
            }
        }
        extremum.calculatePeek()
    }

    /**
     * 重置当前颜色
     */
    private fun resetCurrentColor() {
        if (fenShiDataList!!.isEmpty()) {
            return
        }
        val lastPrice = lastPrice
        when {
            lastPrice > preClose -> {
                config.currentColor = config.upColor
            }
            lastPrice < preClose -> {
                config.currentColor = config.downColor
            }
            else -> {
                config.currentColor = config.equalColor
            }
        }
    }
}