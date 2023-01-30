package com.xu.kotandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseFragment
import com.xu.kotandroid.databinding.FragmentPickPlayersBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class PickPlayersFragment :
    BaseFragment<FragmentPickPlayersBinding>(R.layout.fragment_pick_players) {
    private var param1: String? = null
    private var param2: String? = null
    override fun initView() {
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
            PickPlayersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}