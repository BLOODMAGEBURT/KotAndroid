package com.xu.kotandroid.ui

import android.os.Bundle
import android.text.style.TtsSpan.TimeBuilder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.xu.kotandroid.ui.ui.theme.KotAndroidTheme
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

class PagerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent()
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun MainContent() {
        Column {
            val titles = remember {
                listOf("China", "Hk", "TaiWan")
            }
            val pagerState = rememberPagerState { 3 }
            val scope = rememberCoroutineScope()
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                divider = {},
                indicator = { tabPositions ->
                    PagerTabIndicator(tabPositions, pagerState, color = Color.Cyan)
                },
                modifier = Modifier.height(36.dp).fillMaxWidth()
            ) {
                titles.forEachIndexed { index, title ->
                    PagerTab(pagerState = pagerState,
                        index = index,
                        text = title,
                        modifier = Modifier
                            .height(30.dp)
                            .clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            })
                }
            }
            HorizontalPager(state = pagerState) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "$it Pager")
                }
            }
        }
    }
}

/**
 * PagerTap 指示器
 * @param  percent  指示器占用整个tab宽度的比例
 * @param  height   指示器的高度
 * @param  color    指示器的颜色
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerTabIndicator(
    tabPositions: List<TabPosition>,
    pagerState: PagerState,
    color: Color = MaterialTheme.colorScheme.surface,
    @FloatRange(from = 0.0, to = 1.0) percent: Float = 0.1f,
    height: Dp = 2.dp,
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val currentPage = minOf(tabPositions.lastIndex, pagerState.currentPage)
        val currentTab = tabPositions[currentPage]
        val previousTab = tabPositions.getOrNull(currentPage - 1)
        val nextTab = tabPositions.getOrNull(currentPage + 1)
        val fraction = pagerState.currentPageOffsetFraction

        val indicatorWidth = currentTab.width.toPx() * percent

        val indicatorOffset = if (fraction > 0 && nextTab != null) {
            lerp(currentTab.left, nextTab.left, fraction).toPx()
        } else if (fraction < 0 && previousTab != null) {
            lerp(currentTab.left, previousTab.left, -fraction).toPx()
        } else {
            currentTab.left.toPx()
        }

        Log.e(
            "hj",
            "fraction = $fraction , indicatorOffset = $indicatorOffset"
        )
        val canvasHeight = size.height
        drawRoundRect(
            color = color,
            topLeft = Offset(
                indicatorOffset + (currentTab.width.toPx() * (1 - percent) / 2),
                canvasHeight - height.toPx()
            ),
            size = Size(indicatorWidth + indicatorWidth * abs(fraction*4), height.toPx()),
            cornerRadius = CornerRadius(50f)
        )
    }
}


/**
 * 自定义 PagerTab
 * @param index                     对应第几个tab 从0开始
 * @param selectedContentColor      tab选中时的颜色
 * @param unselectedContentColor    tab没选中时的颜色
 * @param selectedFontSize          tab选中时的文字大小
 * @param unselectedFontSize        tab没选中时的文字大小
 * @param selectedFontWeight        tab选中时的文字比重
 * @param unselectedFontWeight      tab没选中时的文字比重
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerTab(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    index: Int,
    text: String,
    selectedContentColor: Color = MaterialTheme.colorScheme.primary,
    unselectedContentColor: Color = MaterialTheme.colorScheme.onSurface,
    selectedFontSize: TextUnit = 16.sp,
    unselectedFontSize: TextUnit = 14.sp,
    selectedFontWeight: FontWeight = FontWeight.SemiBold,
    unselectedFontWeight: FontWeight = FontWeight.Normal,
) {

    val currentPage = pagerState.currentPage
    val isCurrentPage by remember(currentPage) {
        derivedStateOf { currentPage == index }
    }

    val fraction = pagerState.currentPageOffsetFraction
    val pageOffset = ((currentPage - index) + fraction).absoluteValue

    val progress = 1f - pageOffset.coerceIn(0f, 1f)

    if (index == 1) {
        Log.d("here", "here page: $currentPage  progress:$progress")
    }

    val fontSize = lerp(unselectedFontSize, selectedFontSize, progress)
    val color = lerp(unselectedContentColor, selectedContentColor, progress)
//    val fontWeight = lerp(unselectedFontWeight, selectedFontWeight, progress)


    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            fontSize = fontSize,
            fontWeight = if (isCurrentPage) selectedFontWeight else unselectedFontWeight
        )
    }
}

