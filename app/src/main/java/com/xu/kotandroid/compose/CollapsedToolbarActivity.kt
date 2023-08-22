package com.xu.kotandroid.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xu.kotandroid.compose.ui.theme.KotAndroidTheme
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

class CollapsedToolbarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    CollapsingToolbarScaffold(
                        modifier = Modifier,
                        state = rememberCollapsingToolbarScaffoldState(),
                        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
                        toolbar = {
                            ScrollHeader()
                        }
                    ) {

                        BookList()


                    }
                }
            }
        }
    }

    @Composable
    private fun BookList() {

        LazyColumn(modifier = Modifier.fillMaxWidth()) {

            repeat(10) {
                item {
                    BookItem()
                }
            }

        }

    }

    @Composable
    fun BookItem() {
        Text(text = "shakespeare", modifier = Modifier.padding(vertical = 12.dp))
    }

    @Composable
    private fun ScrollHeader() {

        Box(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onBackground.copy(0.5f))
        )

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    KotAndroidTheme {
        Greeting("Android")
    }
}