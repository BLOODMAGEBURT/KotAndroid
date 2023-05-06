package com.xu.kotandroid.ui

import android.annotation.SuppressLint
import android.text.style.TtsSpan.TimeBuilder
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.LogUtils.I
import com.drake.brv.BindingAdapter
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.item.ItemExpand
import com.drake.brv.layoutmanager.HoverGridLayoutManager
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityMultipleTypeBinding
import com.xu.kotandroid.model.WarrantFilterModel
import com.xu.kotandroid.model.WarrantFilterWithFlowModel

class MultipleTypeActivity :
    BaseActivity<ActivityMultipleTypeBinding>(R.layout.activity_multiple_type) {

    override fun initView() {


        val layoutManager = GridLayoutManager(this, 3) // 3 则代表列表一行铺满要求跨度为3
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position >= (binding.rv.bindingAdapter.models?.size ?: 0)) return 1
                if (position < 0) return 1 // 如果添加分割线可能导致position为负数
                // 根据类型设置列表item跨度
                return when (binding.rv.bindingAdapter.getItemViewType(position)) {
                    R.layout.item_warrant_filter -> 1 // 设置指定类型的跨度为1, 假设spanCount为3则代表此类型占据宽度为3分之一
                    else -> 3
                }
            }
        }
        binding.rv.layoutManager = layoutManager


        binding.rv.divider {
            addType(R.layout.item_warrant_filter)
            setDivider(20)
            orientation = DividerOrientation.GRID
        }.setup {
            expandAnimationEnabled = false
            addType<WarrantFilterWithFlowModel>(R.layout.item_warrant_filter_flow)
            addType<WarrantFilterModel>(R.layout.item_warrant_filter)

            R.id.text.onClick {
                val filterModel = getModel<WarrantFilterModel>()
                filterModel.isActive = !filterModel.isActive
                notifyItemChanged(bindingAdapterPosition)

                val parentPosition = findParentPosition()
                if (parentPosition == -1) {
                    return@onClick
                }
                val parent = getModel<WarrantFilterWithFlowModel>(parentPosition)

                Log.e(
                    "here",
                    "parentPosition:$parentPosition ${parent.canMultiSelected} ${parent.jsonSublist}"
                )
                if (!parent.canMultiSelected && filterModel.isActive) {
                    parent.jsonSublist.forEachIndexed { index, warrantFilterModel ->

                        if (warrantFilterModel.isActive && warrantFilterModel != filterModel) {
                            warrantFilterModel.isActive = false
                            notifyItemChanged(parentPosition + index + 1)
                        }
                    }
                }
            }

            R.id.flowRoot.onFastClick {
                val titleModel = getModel<WarrantFilterWithFlowModel>()
                if (!titleModel.hasMore) {
                    return@onFastClick
                }
                if (titleModel.itemExpand) {
                    collapsedWithKeep(6)
                } else {
                    expandMore(6)
                }
            }
        }
    }

    override fun initData() {

        binding.rv.models = makeList()

    }

    private fun makeList(): List<Any?> {
        val list = mutableListOf<Any?>()
        for (i in 0..10) {

            list.add(WarrantFilterWithFlowModel("$i", "$i", hasMore = i > 5, i % 2 == 0).apply {

                jsonSublist = mutableListOf<WarrantFilterModel>().apply {
                    for (j in 0..i) {
                        add(WarrantFilterModel("$i", "${i + j}", false, j > 5))
                    }
                }
            })
        }
        return list
    }
}


@SuppressLint("NotifyDataSetChanged")
fun BindingAdapter.BindingViewHolder.collapsedWithKeep(keepSize: Int): Int {
    val itemExpand = getModelOrNull<ItemExpand>() ?: return 0
    if (!itemExpand.itemExpand) return 0

    val position = if (bindingAdapterPosition == -1) layoutPosition else bindingAdapterPosition

    val itemSublist = itemExpand.itemSublist
    itemExpand.itemExpand = false

    return if (itemSublist.isNullOrEmpty()) {
        0
    } else {
        val sublistFlat = itemSublist.toMutableList()

//        val from = position + 1
//        (adapter.models as MutableList).subList(
//            from + keepSize,
//            from + sublistFlat.size
//        ).clear()


        sublistFlat.forEachIndexed { index, warrantFilterModel ->
            if (index > keepSize) {
                if (warrantFilterModel is WarrantFilterModel) {
                    warrantFilterModel.isActive = false
                }
            }
        }



        adapter.notifyDataSetChanged()
        sublistFlat.size - keepSize
    }
}

@SuppressLint("NotifyDataSetChanged")
fun collapsedKeep(
    keepSize: Int,
    position: Int,
    itemExpand: ItemExpand,
    adapter: BindingAdapter
): Int {
    if (!itemExpand.itemExpand) return 0
    val itemSublist = itemExpand.itemSublist
    itemExpand.itemExpand = false

    return if (itemSublist.isNullOrEmpty()) {
        0
    } else {
        val sublistFlat = itemSublist.toMutableList()

        val from = position + 1
        (adapter.models as MutableList).subList(
            from + keepSize,
            from + sublistFlat.size
        ).clear()
        adapter.notifyDataSetChanged()
        sublistFlat.size - keepSize
    }
}


@SuppressLint("NotifyDataSetChanged")
fun BindingAdapter.BindingViewHolder.expandMore(keepSize: Int): Int {
    val itemExpand = getModelOrNull<ItemExpand>() ?: return 0
    if (itemExpand.itemExpand) return 0

    val position = if (bindingAdapterPosition == -1) layoutPosition else bindingAdapterPosition
    val itemSublist = itemExpand.itemSublist
    itemExpand.itemExpand = true

    return if (itemSublist.isNullOrEmpty()) {
        0
    } else {
        val sublistFlat = itemSublist.toMutableList().subList(keepSize, itemSublist.size)

//        (adapter.models as MutableList).addAll(position + 1 + keepSize, sublistFlat)

        sublistFlat.forEachIndexed { index, warrantFilterModel ->
            if (index > keepSize) {
                if (warrantFilterModel is WarrantFilterModel) {
                    warrantFilterModel.isActive = true
                }
            }
        }


        adapter.notifyDataSetChanged()

        sublistFlat.size
    }
}


@SuppressLint("NotifyDataSetChanged")
fun BindingAdapter.BindingViewHolder.findParentPosition(own: Boolean): Int {

    for (index in layoutPosition - 1 downTo 0) {
        val item = adapter.models?.getOrNull(index) ?: break
        if (item is ItemExpand && item.itemSublist?.contains(_data) == true) {
            return index
        }
    }
    return -1
}