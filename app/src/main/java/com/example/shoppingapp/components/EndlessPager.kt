package com.example.shoppingapp.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EndlessPager(images:List<String> = emptyList() ) {

    val list = listOf(
        R.drawable.icon_sell,
        R.drawable.icon_image,
        R.drawable.icon_favorite,
        R.drawable.icon_settings,
        R.drawable.icon_cart_garden,
    )

    val pagerState = rememberPagerState(
        pageCount = { Int.MAX_VALUE },
        initialPage = 0
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        CompositionLocalProvider(LocalOverscrollFactory provides null) {
            HorizontalPager(
                modifier = Modifier.height(160.dp),
                pageSpacing = 15.dp,
                contentPadding = PaddingValues(horizontal = 18.dp),
                state = pagerState
            ) { index ->
                images.getOrNull(
                    index % (images.size)
                )?.let { item ->
                    BannerItem(image = item)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        var initPage = Int.MAX_VALUE / 2 - 5000
        while (initPage % list.size != 0) {
            initPage++
        }
        pagerState.scrollToPage(initPage)

        while(true){
            delay(5000)
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }

    }

}

@Composable
fun BannerItem(image: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = image,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth,
            contentDescription = "Banner Image"
        )
    }
}
