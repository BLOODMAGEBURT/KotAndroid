package com.xu.kotandroid.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @Author Xu
 * Date：2022/8/31 13:38
 * Description：
 */
class CalendarViewModel : ViewModel() {

    val addEvent = MutableLiveData<Int>()

}