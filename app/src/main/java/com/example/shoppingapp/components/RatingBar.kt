package com.example.shoppingapp.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import kotlin.math.floor
import kotlin.math.round

@Composable
fun RatingBar(
    rating: Float,
    maxRating: Int = 5,
    iconSize: Int = 16
) {
    Row {
        repeat(maxRating) { index ->
            Icon(
                painter = painterResource(R.drawable.icon_star),
                contentDescription = "average of ${rating.toInt()} on $maxRating",
                tint = if(index < rating.toInt()) AppStyle.colors.gold else Color.LightGray,
                modifier = Modifier.size(iconSize.dp)
            )
        }
    }
}