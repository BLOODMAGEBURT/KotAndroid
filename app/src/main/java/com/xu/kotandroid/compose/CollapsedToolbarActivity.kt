package com.xu.kotandroid.compose

import android.os.Bundle
import android.text.style.TtsSpan.TimeBuilder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xu.kotandroid.R
import com.xu.kotandroid.compose.ui.theme.KotAndroidTheme
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.w3c.dom.Text

class CollapsedToolbarActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val state = rememberCollapsingToolbarScaffoldState()
                    val pagerState = rememberPagerState { 3 }

                    CollapsingToolbarScaffold(
                        modifier = Modifier,
                        state = state,
                        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
                        toolbar = {
                            ScrollHeader(pagerState)
                        }
                    ) {

                        HorizontalPager(state = pagerState) {

                            when (it) {
                                0 -> {
                                    BookList(10, "shakespeare")
                                }

                                1 -> {
                                    AuthorList(20, "liu xiang")
                                }

                                2 -> {
                                    BookList(30, "xu xiao bo")
                                }

                            }

                        }

                    }
                }
            }
        }
    }


    @Composable
    private fun BookList(count: Int, author: String) {

        LazyColumn(modifier = Modifier.fillMaxSize()) {

            items(count) {
                BookItem(author)
            }

        }

    }

    @Composable
    private fun AuthorList(count: Int, author: String) {

        LaunchedEffect(key1 = author) {
            Log.e("here", "bookList author: $author")

        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {

            items(count) {
                AuthorItem(author)
            }

        }

    }

    @Composable
    fun BookItem(author: String) {
        Text(
            text = author,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }

    @Composable
    fun AuthorItem(author: String) {
        Row {
            Text(
                text = author,
                modifier = Modifier
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center
            )
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                repeat(15) {
                    Text(author)
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun CollapsingToolbarScope.ScrollHeader(pagerState: PagerState) {

        Box(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
                .parallax(1f)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.onBackground.copy(0.5f))
        )

        val scope = rememberCoroutineScope()


        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier
                .road(Alignment.BottomCenter, Alignment.BottomCenter)
        ) {
            Text(text = "Tab1", modifier = Modifier.clickable {
                scope.launch {
                    pagerState.animateScrollToPage(0)
                }
            })
            Text(text = "Tab2", modifier = Modifier.clickable {
                scope.launch {
                    pagerState.animateScrollToPage(1)
                }
            })
            Text(text = "Tab3", modifier = Modifier.clickable {
                scope.launch {
                    pagerState.animateScrollToPage(2)
                }
            })
        }

        //        Box(modifier = Modifier.height(0.dp))
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