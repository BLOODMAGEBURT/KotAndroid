package com.xu.kotandroid.ui.model

import androidx.databinding.ObservableBoolean

/**
 * @Author Xu
 * Date：2023/1/31 10:35
 * Description：
 */
data class NewPlayer(
    var playerName: String = "",
    val isHuman: ObservableBoolean = ObservableBoolean(true),
    val canBeRemoved: Boolean = true,
    val canBeToggled: Boolean = true,
    var isInclude: ObservableBoolean = ObservableBoolean(!canBeRemoved),
    var selectedAIPosition: Int = -1,
) {

    fun selectAI() = if (!isHuman.get()) AI.basicAI.getOrNull(selectedAIPosition) else null


}
