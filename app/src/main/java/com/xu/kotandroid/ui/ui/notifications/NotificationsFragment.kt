package com.xu.kotandroid.ui.ui.notifications


import android.widget.TextView
import androidx.fragment.app.viewModels
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseFragment
import com.xu.kotandroid.databinding.FragmentNotificationsBinding

class NotificationsFragment :
    BaseFragment<FragmentNotificationsBinding>(R.layout.fragment_notifications) {

    private val vm by viewModels<NotificationsViewModel>()

    override fun initView() {
        val textView: TextView = binding.textNotifications
        vm.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

    override fun initData() {
    }
}