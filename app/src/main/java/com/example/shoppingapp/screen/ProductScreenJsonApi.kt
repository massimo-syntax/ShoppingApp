package com.example.shoppingapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.components.BackButtonSimpleTopBar
import com.example.shoppingapp.data.model.UiProductWithFieldsFromRoom
import com.example.shoppingapp.repository.RemoteProductsRepository
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.R
import com.example.shoppingapp.Routes
import com.example.shoppingapp.components.RatingBar
import com.example.shoppingapp.viewmodel.RatingsViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random


@Composable
fun ProductScreenJsonApi(id: String, ratingsViewModel: RatingsViewModel = viewModel()) {

    val roomRepo = SelectedProductsRepository(LocalContext.current)
    val remoteRepo = RemoteProductsRepository()

    var inCart by remember { mutableStateOf(false) }
    var inFav by remember { mutableStateOf(false) }

    var product by remember { mutableStateOf<UiProductWithFieldsFromRoom?>(null) }
    val ratingsUiState by ratingsViewModel.ratings.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {

        val remoteProduct = remoteRepo.getProduct(id)
        ratingsViewModel.getRatings(id)

        val fav = roomRepo.getOneFav(remoteProduct.id)
        val cart = roomRepo.getOneCart(remoteProduct.id)

        inCart = cart != null && cart.productId == remoteProduct.id
        inFav = fav != null && fav.productId == remoteProduct.id

        val rating = String.format("%.2f", Random.nextFloat())

        val uiProduct = UiProductWithFieldsFromRoom(
            id = remoteProduct.id,
            title = remoteProduct.title,
            description = remoteProduct.description,
            images = remoteProduct.image,
            price = remoteProduct.image,
            category = remoteProduct.category,
            rating = rating,
            userId = "",
            cart = inCart,
            fav = inFav
        )

        product = uiProduct

    }



    Scaffold(
        topBar = {
            BackButtonSimpleTopBar("Product", false)
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingScaffold ->
        Column(
            modifier = Modifier
                .padding(paddingScaffold)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            if (product != null) {

                val product = product!!

                val imagesBoxHeight = 256.dp
                // image
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(imagesBoxHeight)
                ) {
                    AsyncImage(
                        model = product.images,
                        contentDescription = "image of" + product.title,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(imagesBoxHeight),
                    )

                    // icon fav on picture
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = {
                                    runBlocking {
                                        if (!inFav) roomRepo.addToFav(product.id)
                                        else roomRepo.deleteFromFav(product.id)
                                        inFav = !inFav
                                    }

                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.icon_favorite),
                                    contentDescription = "favorite",
                                    tint = if (inFav) AppStyle.colors.red else Color.White,
                                    modifier = Modifier.size(36.dp)

                                )
                            }
                        }
                    }
                }


                Column(
                    Modifier.padding(16.dp)
                ) {

                    Spacer(Modifier.height(16.dp))
                    // title
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleLarge,
                    )

                    Spacer(Modifier.height(16.dp))

                    // description

                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Spacer(Modifier.height(46.dp))

                    // rating
                    if (ratingsUiState.loading)
                        RatingBar(0f)
                    else if (ratingsUiState.result.isNotEmpty()) {
                        // calculate average
                        val size = ratingsUiState.result.size
                        var reducer = 0f
                        for (rating in ratingsUiState.result) {
                            reducer += rating.rating
                        }
                        val average = reducer / size
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RatingBar(
                                    rating = average,
                                    iconSize = 28
                                )
                                Text(average.toString())
                            }
                            TextButton(
                                onClick = {}
                            ) {
                                Text("See all reviews")
                            }

                        }

                    } else
                        Text(
                            text = "No ratings, buy and be the first to write a review!",
                            style = MaterialTheme.typography.bodyMedium,
                        )

                    Spacer(Modifier.height(46.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                if (!inCart) roomRepo.addToCart(product.id)
                                else roomRepo.deleteFromCart(product.id)
                                inCart = !inCart
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AppStyle.colors.darkBlule),
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_cart_garden),
                            contentDescription = "rating icon on button",
                            tint = if (!inCart) AppStyle.colors.green else AppStyle.colors.red,
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(Modifier.width(6.dp))

                        Text(
                            text = if (inCart) "remove from cart" else "add to cart"
                        )
                    }
                    Spacer(Modifier.height(16.dp))

                }
            }// product != null
        }
    }// scaffold
}


