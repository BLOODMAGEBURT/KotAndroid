package com.xu.kotandroid.compose.questions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xu.kotandroid.R

/**
 * @Author Xu
 * Date：2023/6/25 11:13
 * Description：
 */
@Composable
fun MultiChoiceQuestion(
    selectedAnswers: List<Int> = emptyList(),
    onOptionSelected: (Boolean, Int) -> Unit
) {

    listOf(
        R.string.read,
        R.string.work_out,
        R.string.draw,
        R.string.play_games,
        R.string.dance,
        R.string.watch_movies,
    ).forEachIndexed { _, stringId ->

        val checked = selectedAnswers.contains(stringId)
        CheckboxRow(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            text = stringResource(id = stringId),
            isChecked = checked,
            onOptionSelected = {
                onOptionSelected(!checked, stringId)
            })
    }

}

@Composable
fun CheckboxRow(
    text: String,
    isChecked: Boolean,
    onOptionSelected: () -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        color = if (isChecked) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },

        border = BorderStroke(
            1.dp,
            if (isChecked) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        ),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOptionSelected() },
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {

            Text(
                text = text, modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )

            Checkbox(
                checked = isChecked,
                modifier = Modifier.padding(start = 8.dp),
                onCheckedChange = null
            )
        }
    }


}