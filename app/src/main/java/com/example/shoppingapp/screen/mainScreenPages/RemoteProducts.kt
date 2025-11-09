package com.example.shoppingapp.screen.mainScreenPages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.Routes
import com.example.shoppingapp.components.CustomTextField
import com.example.shoppingapp.data.model.UiProductWithFieldsFromRoom
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.viewmodel.RemoteProductsViewModel
import kotlinx.coroutines.launch


@Composable
fun RemoteProducts(modifier: Modifier = Modifier, updateBadge:(n:Int)->Unit, viewModel: RemoteProductsViewModel = viewModel()) {

    // get cart and fav from ROOM
    val roomRepo = SelectedProductsRepository(LocalContext.current)
    var cartCount by remember { mutableStateOf(0) }

    // products from JSON api
    var uiProducts by viewModel.uiProducts

    // query from textfield
    var searchQuery by remember { mutableStateOf("") }

    // update room and ui when cart or fav is pressed,
    val coroutineScope = rememberCoroutineScope()

    // get products async, request also cart and favourites from room
    LaunchedEffect(Unit) {
        viewModel.fetchRemoteProducts(roomRepo)
        cartCount = roomRepo.getCart().size
    }


    Column(modifier = modifier.padding(horizontal = 10.dp).background(Color.White)) {
        // SEARCH text field
        val focusManager = LocalFocusManager.current
        Row {
            CustomTextField(
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.height(36.dp).width(36.dp),
                        onClick = {
                            focusManager.clearFocus()
                            searchQuery = ""
                        }) {
                        if(searchQuery.isEmpty())
                            Icon(
                                painterResource(R.drawable.icon_search),
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
                valueChange = {
                    searchQuery = it
                },
                text = searchQuery
            )
        }

        Spacer(Modifier.height(8.dp))

        if (uiProducts.isEmpty()) {
            Row (
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(36.dp),
                    color = AppStyle.colors.darkBlule
                )
            }
        } else {
            // result grid
            LazyVerticalGrid (
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp),
            ) {
                val filtered  = uiProducts.filter { it.title.lowercase().contains(searchQuery.lowercase()) }
                items(filtered ){ product ->
                    val index = uiProducts.indexOf( product )
                    ProductCard(
                        product = product,
                        isInCart = product.cart,
                        onToggleCart = {
                            coroutineScope.launch {
                                if(!product.cart){
                                    roomRepo.addToCart(product.id)
                                    cartCount++
                                }
                                else{
                                    roomRepo.deleteFromCart(product.id)
                                    cartCount--
                                }
                                updateBadge(cartCount)

                                // UPDATE LIST UI, TRIGGER RECOMPOSITION
                                // new List
                                val newList = uiProducts.toMutableList()
                                // toggle cart boolean of UI product object
                                newList[index] = product.copy(cart = !product.cart)
                                // recompose hence list not same
                                uiProducts = newList

                            }

                        },
                        isInFav = product.fav,
                        onToggleFav = {
                            coroutineScope.launch {
                                if(!product.fav) roomRepo.addToFav(product.id)
                                else roomRepo.deleteFromFav(product.id)
                                // UPDATE LIST UI, TRIGGER RECOMPOSITION
                                // new List
                                val newList = uiProducts.toMutableList()
                                // toggle fav boolean of UI product object
                                newList[index] = product.copy(fav= !product.fav)
                                // recompose hence list not same
                                uiProducts = newList
                            }

                        }
                    )
                }
            }
        }
    }
}




@Composable
fun ProductCard(
    product: UiProductWithFieldsFromRoom,
    onToggleCart: () -> Unit,
    onToggleFav: () -> Unit,
    isInCart: Boolean,
    isInFav:Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier.padding(4.dp).fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // icon + image
            // keep icon on the image
            Box {
                AsyncImage(
                    // here from json there is only 1 image
                    model = product.images,
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
                // icon at top end of image
                Row(
                    modifier = Modifier.fillMaxWidth().padding(6.dp),
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
            Column(modifier = Modifier.padding(6.dp)) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium,
                    lineHeight = 20.sp,
                    maxLines = 2,
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    // see button
                    OutlinedButton(
                        onClick = {
                            Routes.navController.navigate(Routes.productJsonApi+"/"+product.id)
                        },
                        modifier = Modifier.height(36.dp).width(100.dp)
                    ) {
                        Icon(
                            painterResource( R.drawable.icon_settings),
                            contentDescription = if (isInCart) "Added" else "Add to Cart",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text("View")
                    }

                    // add to cart icon
                    IconButton(
                        onClick = onToggleCart,
                    ) {
                        Icon(
                            painterResource( R.drawable.icon_cart_garden),
                            contentDescription = if (isInCart) "Added" else "Add to Cart",
                            tint = if(isInCart) Color.Blue else Color.LightGray
                        )
                    }
                }
            }
        }
    }
}







