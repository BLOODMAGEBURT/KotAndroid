package com.xu.kotandroid.ui

import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xu.kotandroid.R
import com.xu.kotandroid.base.BaseActivity
import com.xu.kotandroid.databinding.ActivityPennyBinding

class PennyActivity : BaseActivity<ActivityPennyBinding>(R.layout.activity_penny) {


    override fun initView() {

        val navView: BottomNavigationView = binding.bottomNav
        val navController = findNavController(R.id.containerFragment)
        navView.setupWithNavController(navController)
    }


    override fun initData() {

    }

}