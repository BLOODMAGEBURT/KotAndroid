package com.xu.kotandroid.compose

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.xu.kotandroid.compose.questions.Superhero

/**
 * @Author Xu
 * Date：2023/6/14 11:22
 * Description：
 */
class SurveyViewModel : ViewModel() {

    private val questionOrder: List<SurveyQuestion> = listOf(
        SurveyQuestion.FREE_TIME,
        SurveyQuestion.SUPERHERO,
        SurveyQuestion.LAST_TAKEAWAY,
        SurveyQuestion.FEELING_ABOUT_SELFIES,
        SurveyQuestion.TAKE_SELFIE,
    )

    private var questionIndex = 0

    private val _freeTimeResponse = mutableStateListOf<Int>()

    private val _isNextAble = mutableStateOf(false)

    private val _showDone = mutableStateOf(false)

    val freeTimeResponse: List<Int>
        get() = _freeTimeResponse

    val isNextAble: Boolean
        get() = _isNextAble.value

    val showDone: Boolean
        get() = _showDone.value

    fun onFreeTimeResponse(selected: Boolean, answer: Int) {
        if (selected) {
            _freeTimeResponse.add(answer)
        } else {
            _freeTimeResponse.remove(answer)
        }
        _isNextAble.value = _freeTimeResponse.isNotEmpty()
    }

    fun onHeroResponse(hero: Superhero) {

    }


    fun onNextPressed() {
        questionIndex++
    }

    fun onDone() {
        _showDone.value = true
    }
}

enum class SurveyQuestion {
    FREE_TIME,
    SUPERHERO,
    LAST_TAKEAWAY,
    FEELING_ABOUT_SELFIES,
    TAKE_SELFIE,
}