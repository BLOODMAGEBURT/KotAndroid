package com.xu.kotandroid.model

import androidx.databinding.BaseObservable
import com.drake.brv.item.ItemExpand
import com.xu.kotandroid.R

/**
 * @Author Xu
 * Date：2023/4/4 10:53
 * Description：
 */
data class WarrantFilterModel(
    val parent: WarrantFilterWithFlowModel,
    val code: String = "",
    val text: String,
    var isActive: Boolean = false,
    var willGone: Boolean = false
)

data class WarrantFilterWithFlowModel(
    val tag: String,
    val text: String,
    var hasMore: Boolean = false,
    val canMultiSelected: Boolean = true,
) : ItemParent, BaseObservable() {


    //    /** 接口数据里面的子列表使用此字段接收(请注意避免gson等框架解析kotlin会覆盖字段默认值问题) */
    var jsonSublist: List<WarrantFilterModel> = emptyList()

    //
    override var itemExpand: Boolean = false
        set(value) {
            field = value
            notifyChange()
        }
    override var itemSublist: List<Any?>
        get() = jsonSublist
        set(value) {
            jsonSublist = value as List<WarrantFilterModel> // 注意类型转换异常
        }

    val expandIcon get() = if (itemExpand) R.drawable.arrow_up else R.drawable.arrow_down

}
