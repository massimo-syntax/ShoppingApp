package com.example.shoppingapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.Routes
import com.example.shoppingapp.data.model.UiProductWithFieldsFromRoom


@Composable
fun MainProductCard(
    modifier:Modifier = Modifier,
    product: UiProductWithFieldsFromRoom,
    onToggleFav: () -> Unit,
    isInFav:Boolean,
) {

    Column(
        modifier = modifier
            .background(Color.Transparent)
            .width(100.dp)
            .clickable(onClick = {
                Routes.navController.navigate(Routes.productUploaded+"/"+product.id)
            })
    ) {
        // icon + image
        // keep icon on the image
        Box {
            AsyncImage(
                // here from json there is only 1 image
                model = product.images.split(",").first(),
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .width(100.dp)
                    .height(126.dp)
            )
            // icon at top end of image
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onToggleFav,
                ) {
                    Icon(
                        painterResource(R.drawable.icon_favorite),
                        contentDescription = if (isInFav) "Added" else "Add to Favorites",
                        tint = if (isInFav) Color.Red else Color.LightGray
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        // Title price add to cart icon
        Column(modifier = Modifier.padding(0.dp)) {

            Text(
                text = "€ " + product.price,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = AppStyle.colors.green,
                )
            )
            Text(
                text = product.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(0.dp))
            Text(
                text = product.description,
                style = TextStyle(fontWeight = FontWeight.Light),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

        }
    }

}



