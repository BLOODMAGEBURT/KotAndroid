package com.xu.kotandroid.ui

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.xu.kotandroid.BlankFragment
import com.xu.kotandroid.R
import com.xu.kotandroid.adapter.SlidePagerAdapter
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityCalenderBinding
import java.util.ArrayList

class CalenderActivity : BaseActivity<ActivityCalenderBinding>(R.layout.activity_calender) {


    override fun initView() {

        initTab()

    }

    private fun initTab() {
        val tabs = arrayOf("要闻", "快讯", "自选", "日历", "新股", "异动")
        val ipoSellingFragment1: BlankFragment = BlankFragment.newInstance()
        val ipoSellingFragment2: BlankFragment = BlankFragment.newInstance()
        val ipoSellingFragment3: BlankFragment = BlankFragment.newInstance()
        val ipoSellingFragment4: CalendarFragment = CalendarFragment.newInstance()
        val ipoSellingFragment5: BlankFragment = BlankFragment.newInstance()
        val ipoSellingFragment6: BlankFragment = BlankFragment.newInstance()
        val fragments = ArrayList<Fragment>()
        fragments.add(ipoSellingFragment1)
        fragments.add(ipoSellingFragment2)
        fragments.add(ipoSellingFragment3)
        fragments.add(ipoSellingFragment4)
        fragments.add(ipoSellingFragment5)
        fragments.add(ipoSellingFragment6)

        val slidePagerAdapter = SlidePagerAdapter(this)
        slidePagerAdapter.setData(fragments)
        binding.pager.adapter = slidePagerAdapter


        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

    }

    override fun initData() {
    }
}