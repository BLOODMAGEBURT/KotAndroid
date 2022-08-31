package com.xu.kotandroid.ui

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.xu.kotandroid.BlankFragment
import com.xu.kotandroid.R
import com.xu.kotandroid.adapter.SlidePagerAdapter
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityCalenderBinding
import com.xu.kotandroid.util.CalendarEventsUtils
import com.xu.kotandroid.vm.CalendarViewModel
import java.util.*

class CalenderActivity : BaseActivity<ActivityCalenderBinding>(R.layout.activity_calender) {

    private val vm by viewModels<CalendarViewModel>()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            if (results[Manifest.permission.WRITE_CALENDAR] == true && results[Manifest.permission.READ_CALENDAR] == true) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                Log.e("here", "auth true")
                addEvent()
            } else {
                Log.e("here", "auth false")
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.

            }
        }


    override fun initView() {

        initTab()
        initListener()

    }

    private fun initListener() {

        vm.addEvent.observe(this) {
            toAdd()
        }

    }

    private fun initTab() {
        val tabs = arrayOf("要闻", "快讯", "自选", "日历", "新股", "异动")
        val ipoSellingFragment1 = BlankFragment()
        val ipoSellingFragment2 = BlankFragment()
        val ipoSellingFragment3 = BlankFragment()
        val ipoSellingFragment4: CalendarFragment = CalendarFragment.newInstance()
        val ipoSellingFragment5 = BlankFragment()
        val ipoSellingFragment6 = BlankFragment()
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

    private fun toAdd() {
        val flag = !((ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_CALENDAR
        ) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CALENDAR
        ) != PackageManager.PERMISSION_GRANTED))

        if (flag) {
            addEvent()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.WRITE_CALENDAR,
                    Manifest.permission.READ_CALENDAR
                )
            )
        }
    }

    private fun addEvent() {
        Log.d("here", "addEvent")
        val beginTime: Calendar = Calendar.getInstance()
        beginTime.set(2022, 9, 15, 12, 0)
        val startMillis: Long = beginTime.timeInMillis
        val endTime: Calendar = Calendar.getInstance()
        endTime.set(2022, 9, 20, 15, 0)
        val endMillis: Long = endTime.timeInMillis
        CalendarEventsUtils.addCalendarEvent(
            this,
            "火车票",
            "别拖拖拉拉的，还有5分钟火车就开动了，抓紧～",
            startMillis,
            endMillis,
            5
        ).let {

            Toast.makeText(this, if (it) "创建成功" else "创建失败", Toast.LENGTH_SHORT).show()

        }

    }
}