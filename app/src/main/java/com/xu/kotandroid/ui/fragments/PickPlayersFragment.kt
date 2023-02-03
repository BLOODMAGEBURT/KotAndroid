package com.xu.kotandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseFragment
import com.xu.kotandroid.databinding.FragmentPickPlayersBinding
import com.xu.kotandroid.ui.vm.PickPlayersViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class PickPlayersFragment :
    BaseFragment<FragmentPickPlayersBinding>(R.layout.fragment_pick_players) {
    private var param1: String? = null
    private var param2: String? = null

    private val vm by activityViewModels<PickPlayersViewModel>()


    override fun initView() {
        // 绑定binding
        binding.vm = vm

        binding.fab.setOnClickListener {
            Log.d("ss",
                vm.players.value!!.joinToString(",") { it.selectedAIPosition.toString() })
            findNavController().navigate(R.id.gameFragment)
        }
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