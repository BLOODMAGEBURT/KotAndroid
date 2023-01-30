package com.xu.kotandroid.ui.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseFragment
import com.xu.kotandroid.databinding.FragmentGameBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GameFragment :
    BaseFragment<FragmentGameBinding>(R.layout.fragment_game) {
    private var param1: String? = null
    private var param2: String? = null
    override fun initView() {

        // 添加滚动
        binding.textCurrentTurnInfo.movementMethod = ScrollingMovementMethod()

    }

    override fun initData() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}