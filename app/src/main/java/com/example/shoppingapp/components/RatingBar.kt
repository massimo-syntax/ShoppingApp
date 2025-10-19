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

@Composable
fun RatingBar(
    rating: Float,
    maxRating: Int = 5
) {
    Row {
        repeat(maxRating) { index ->
            Icon(
                painter = if (index < rating.toInt()) painterResource(R.drawable.icon_star) else painterResource(R.drawable.icon_settings),
                contentDescription = "average of ${rating.toInt()} on $maxRating",
                tint = if(index < rating.toInt()) AppStyle.colors.gold else Color.LightGray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}