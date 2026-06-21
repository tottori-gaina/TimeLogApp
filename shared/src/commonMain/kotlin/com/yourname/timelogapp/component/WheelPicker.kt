package com.yourname.timelogapp.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

@Composable
fun WheelPicker(
    items: List<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val itemHeight = 36.dp
    val visibleItems = 3
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = maxOf(0, selectedIndex - visibleItems / 2)
    )

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val centerIndex = listState.firstVisibleItemIndex + visibleItems / 2
            val clampedIndex = centerIndex.coerceIn(0, items.size - 1)
            onSelectedIndexChange(clampedIndex)
            listState.animateScrollToItem(maxOf(0, clampedIndex - visibleItems / 2))
        }
    }

    Box(
        modifier = modifier.height(itemHeight * visibleItems),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(visibleItems / 2) {
                Box(modifier = Modifier.height(itemHeight))
            }
            items(items.size) { index ->
                val centerIndex = listState.firstVisibleItemIndex + visibleItems / 2
                val distance = abs(index - centerIndex)
                val alpha = when (distance) {
                    0 -> 1f
                    1 -> 0.6f
                    else -> 0.3f
                }
                val fontSize = when (distance) {
                    0 -> 22.sp
                    1 -> 18.sp
                    else -> 14.sp
                }
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .alpha(alpha),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = items[index],
                        fontSize = fontSize,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            items(visibleItems / 2) {
                Box(modifier = Modifier.height(itemHeight))
            }
        }
    }
}