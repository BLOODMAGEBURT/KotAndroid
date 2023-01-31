package com.xu.kotandroid.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xu.kotandroid.ui.model.NewPlayer

/**
 * @Author Xu
 * Date：2023/1/31 10:08
 * Description：viewModel for PickPlayersFragment
 */
class PickPlayersViewModel : ViewModel() {

    val players = MutableLiveData<List<NewPlayer>>().apply {
        this.value = (1..6).map {
            NewPlayer(
                canBeRemoved = it > 2,
                canBeToggled = it > 1
            )
        }
    }


}