package com.xu.kotandroid.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xu.kotandroid.compose.questions.MultiChoiceQuestion
import com.xu.kotandroid.compose.ui.theme.KotAndroidTheme
import com.xu.kotandroid.compose.ui.theme.slightlyDeemphasizedAlpha
import com.xu.kotandroid.compose.ui.theme.stronglyDeemphasizedAlpha
import org.w3c.dom.Text

class SurveyActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Survey()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Survey(modifier: Modifier = Modifier) {

    val vm = SurveyViewModel()
    Scaffold(
        topBar = {
            TopBar(modifier)
        },
        bottomBar = {
            BottomBar(showPrevious = true, showDone = vm.showDone, nextAble = vm.isNextAble)
        }
    ) { paddingValues ->
        SurveyContent(paddingValues, vm.freeTimeResponse, vm::onFreeTimeResponse)
    }
}


@Composable
fun SurveyHeader(title: String, desc: String, modifier: Modifier = Modifier) {

    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(Modifier.height(32.dp))

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = slightlyDeemphasizedAlpha),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    shape = MaterialTheme.shapes.small
                )
                .padding(vertical = 24.dp, horizontal = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = desc,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = stronglyDeemphasizedAlpha),
            modifier = Modifier.padding(start = 16.dp, top = 18.dp, bottom = 10.dp),
            style = MaterialTheme.typography.bodySmall
        )

        Row {
            Text(
                text = "Hello World Hello World",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .weight(1f),
            )
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .weight(1f)
            ) {
                Text(
                    text = "Hello World",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.secondary)
                )
                Text(
                    text = "Hello World",
                    style = MaterialTheme.typography.bodySmall.copy(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
//                        lineHeight = 20.sp,
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        )
                    ),
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.tertiary)
                )
            }

            Text(
                text = "Hello World",
                style = MaterialTheme.typography.bodySmall.copy(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .weight(1f)
            )
        }

    }

}

@Composable
fun SurveyContent(
    paddingValues: PaddingValues,
    selectedAnswers: List<Int> = emptyList(),
    onOptionSelected: (Boolean, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {

        SurveyHeader(
            title = "In my free time i like to ...",
            desc = "Select all that apply",
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        MultiChoiceQuestion(onOptionSelected = onOptionSelected, selectedAnswers = selectedAnswers)
    }
}


@Composable
fun BottomBar(
    showPrevious: Boolean,
    showDone: Boolean,
    nextAble: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 7.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {

            if (showPrevious) {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Previous",
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }

            if (showDone) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(text = "Done")
                }

            } else {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    enabled = nextAble
                ) {
                    Text(text = "Next")
                }
            }
        }
    }

}

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Text(text = "TopBar")
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotAndroidTheme {
        Survey()
    }
}