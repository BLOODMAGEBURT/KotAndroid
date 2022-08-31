package com.xu.kotandroid

import androidx.fragment.app.activityViewModels
import com.xu.kotandroid.base.BaseFragment
import com.xu.kotandroid.databinding.FragmentBlankBinding
import com.xu.kotandroid.vm.CalendarViewModel


class BlankFragment : BaseFragment<FragmentBlankBinding>(R.layout.fragment_blank) {

    val parentVm by activityViewModels<CalendarViewModel>()

    override fun initView() {
        binding.add.setOnClickListener { parentVm.addEvent.value = 1 }
    }

    override fun initData() {
    }
}