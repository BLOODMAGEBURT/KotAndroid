package com.xu.kotandroid.bean


import com.google.gson.annotations.SerializedName

data class FenShiBean(
    @SerializedName("preClose")
    var preClose: Double,
    @SerializedName("symbol")
    var symbol: String,
    @SerializedName("trends")
    var trends: List<Trend>
) {
    data class Trend(
        @SerializedName("change")
        var change: Double,
        @SerializedName("price")
        var price: Double,
        @SerializedName("time")
        var time: String
    )
}