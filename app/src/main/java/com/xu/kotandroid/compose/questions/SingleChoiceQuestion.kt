package com.xu.kotandroid.compose.questions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/**
 * @Author Xu
 * Date：2023/6/25 10:52
 * Description：
 */
@Composable
fun SingleChoiceQuestion(
    possibleHeroes: List<Superhero>,
    selectedAnswer: Superhero?,
    onOptionSelected: (Int) -> Unit
) {

    possibleHeroes.forEach {

        val isSelected = it.id == selectedAnswer?.id

        RadioButtonWithImageRow(
            modifier = Modifier.padding(vertical = 8.dp),
            isSelected = isSelected,
            image = it.imageResourceId,
            text = it.stringResourceId,
            onOptionSelected = { onOptionSelected(it.id) }
        )
    }

}


@Composable
fun RadioButtonWithImageRow(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    @DrawableRes image: Int,
    @StringRes text: Int,
    onOptionSelected: () -> Unit,
) {

    Surface(
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            1.dp, if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        ),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOptionSelected() }
    ) {
        Row(modifier = modifier.padding(16.dp)) {
            Image(painter = painterResource(id = image), contentDescription = "image")
            Text(text = stringResource(id = text), modifier = Modifier.weight(1f))
            RadioButton(selected = isSelected, onClick = null)
        }
    }
}

data class Superhero(
    val id: Int,
    @StringRes val stringResourceId: Int,
    @DrawableRes val imageResourceId: Int
)