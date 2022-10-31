package com.xu.kotandroid.ui.ui.home

import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseFragment
import com.xu.kotandroid.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val vm by viewModels<HomeViewModel>()

    override fun initView() {
        val textView: TextView = binding.textHome
        vm.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

    override fun initData() {
    }
}