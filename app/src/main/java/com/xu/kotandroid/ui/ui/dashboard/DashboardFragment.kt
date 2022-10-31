package com.xu.kotandroid.ui.ui.dashboard

import android.widget.TextView
import androidx.fragment.app.viewModels
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseFragment
import com.xu.kotandroid.databinding.FragmentDashboardBinding

class DashboardFragment : BaseFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {

    private val vm by viewModels<DashboardViewModel>()
    override fun initView() {
        val textView: TextView = binding.textDashboard
        vm.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

    override fun initData() {
    }
}