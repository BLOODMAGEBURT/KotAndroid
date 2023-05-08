package com.xu.kotandroid.model

/**
 * @Author Xu
 * Date：2023/5/8 11:27
 * Description：
 */
interface ItemParent {
    /** 子列表 */
    val itemSublist: List<Any?>

    /** 是否已展开 */
    var itemExpand: Boolean
}