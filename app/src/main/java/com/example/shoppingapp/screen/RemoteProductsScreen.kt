package com.example.shoppingapp.screen

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.components.CustomTextField
import com.example.shoppingapp.data.remote.Product
import com.example.shoppingapp.viewmodel.RemoteProductsViewModel

@Composable
fun RemoteProductsScreen(modifier: Modifier = Modifier, viewModel: RemoteProductsViewModel = viewModel()) {

    val products by viewModel.products

    val cart = mutableListOf( 1, 3 ,5 ,9)

    val searchQuery = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchProducts()

    }

    Column (
        modifier.padding(horizontal = 8.dp),
    ) {

        // Text field
        val focusManager = LocalFocusManager.current
        Row (Modifier.padding(10.dp)) {
            CustomTextField(
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.height(36.dp).width(36.dp),
                        onClick = {
                            focusManager.clearFocus()
                            searchQuery.value = ""
                        }) {
                        if(searchQuery.value.isEmpty())
                            Icon(
                                painterResource(R.drawable.search),
                                contentDescription = "Search",
                                tint = AppStyle.colors.darkBlule,
                                modifier = Modifier.height(36.dp)
                            )
                        else
                            Icon(
                                painterResource(R.drawable.icon_undo),
                                contentDescription = "Exit search",
                                tint = AppStyle.colors.darkBlule,
                                modifier = Modifier.height(36.dp)
                            )
                    }
                },
                text = searchQuery
            )
        }

        // Result
        if (products.isEmpty()) {
            Row (
                Modifier.padding(32.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(36.dp),
                    color = AppStyle.colors.darkBlule
                )
            }

        } else {
            // Display the list of credit cards
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            ) {
                val filtered  = products.filter { it.title.lowercase().contains(searchQuery.value.lowercase()) }
                items(filtered){ product ->
                   /* val isInCart = cart.contains( products.indexOf(product) )
                    ProductCard(
                        product =product,
                        isInCart = isInCart,
                        onAddToCart = {
                            if (isInCart) cart.remove(products.indexOf(product))
                            else cart.add(products.indexOf(product))
                        }
                    )*/
                  Text(product.title)
                }

            }
        }

    }
}


@Composable
fun RatingBar(
    rating: Float,
    maxRating: Int = 5
) {
    Row {
        repeat(maxRating) { index ->
            Icon(
                painter = if (index < rating.toInt()) painterResource(R.drawable.icon_settings) else painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                tint = Color(0xFFFFC107), // Gold
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
@Composable
fun ProductCard(
    product: Product,
    onAddToCart: () -> Unit,
    isInCart: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RatingBar(rating = product.rating?.toFloat() ?: 0.0.toFloat()  )

                    // Animated Add to Cart Button
                    AnimatedAddToCartButton(
                        isAdded = isInCart,
                        onToggle = onAddToCart
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedAddToCartButton(
    isAdded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(targetState = isAdded, label = "AddToCartTransition")

    val scale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300, easing = EaseOutBack) },
        label = "Scale"
    ) { added -> if (added) 1.2f else 1f }


    //val image = AnimatedImageVector.animatedVectorResource(R.drawable.ic_hourglass_animated)

    val icon by transition.animateInt (
        transitionSpec = { tween(durationMillis = 300) },
        label = "IconTransition"
    ) { added ->
        if (added) R.drawable.icon_cart_garden else R.drawable.adaptivecarticon_foreground
    }

    val tint by transition.animateColor(
        transitionSpec = { tween(durationMillis = 300) },
        label = "ColorTransition"
    ) { added ->
        if (added) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
    }

    IconButton(
        onClick = onToggle,
        modifier = modifier.scale(scale)
    ) {
        Icon(
            painterResource(icon),
            contentDescription = if (isAdded) "Added" else "Add to Cart",
            tint = tint
        )
    }
}




