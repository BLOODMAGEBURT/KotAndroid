package com.xu.kotandroid.ui.model

/**
 * @Author Xu
 * Date：2023/1/31 11:37
 * Description：
 */
data class AI(val name: String) {


    companion object {
        @JvmStatic
        val basicAI = listOf(
            AI("TwoFace"),
            AI("PinkFly"),
            AI("fly"),
            AI("inf"),
            AI("sky"),
            AI("000"),
            AI("moon"),
        )

    }

}
