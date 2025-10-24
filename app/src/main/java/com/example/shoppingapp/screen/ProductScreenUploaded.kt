package com.example.shoppingapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shoppingapp.components.BackButtonSimpleTopBar
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.R
import com.example.shoppingapp.components.RatingBar
import com.example.shoppingapp.viewmodel.ProductsViewModel
import kotlinx.coroutines.runBlocking


@Composable
fun ProductScreenUploaded(id:String , viewModel: ProductsViewModel = viewModel() ){

    val roomRepo = SelectedProductsRepository(LocalContext.current)

    var inCart by remember { mutableStateOf(false) }
    var inFav by remember { mutableStateOf(false) }

    // it was not much pain to create a UIState also for single product..
    // so that is just the first in the list, anyways i would first ask to the senior
    val uiState by viewModel.uiProducts.collectAsState()

    LaunchedEffect(Unit) {
        inCart = roomRepo.getOneCart(id) != null
        inFav = roomRepo.getOneFav(id) != null
        viewModel.getProduct(id)
    }


    Scaffold(
        topBar = {
            BackButtonSimpleTopBar("Product", false)
        },
        modifier = Modifier.fillMaxSize(),
    ){ paddingScaffold ->
        Column(
            modifier = Modifier
                .padding(paddingScaffold)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            if (!uiState.fetching && uiState.result.isNotEmpty()) {

                val product = uiState.result.first()

                AsyncImage(product.images, contentDescription = "image of" + product.title)

                Text(product.rating)

                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    IconButton(
                        onClick = {
                            runBlocking {
                                if(!inCart) roomRepo.dropInCart(product.id)
                                else roomRepo.deleteFromCart(product.id)
                                inCart = !inCart
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_cart_garden),
                            contentDescription = "cart",
                            tint = if (inCart) Color.Blue else Color.LightGray
                        )
                    }

                    IconButton(
                        onClick = {
                            runBlocking {
                                if(!inFav) roomRepo.addToFav(product.id)
                                else roomRepo.deleteFromFav(product.id)
                                inFav = !inFav
                            }

                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_favorite),
                            contentDescription = "favorite",
                            tint = if (inFav) Color.Red else Color.LightGray
                        )
                    }
                }

                RatingBar(product.rating.toFloat())

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        runBlocking {
                            if(!inCart) roomRepo.dropInCart(product.id)
                            else roomRepo.deleteFromCart(product.id)
                            inCart = !inCart
                        }
                    }
                ){
                    Icon(
                        painter = painterResource(R.drawable.icon_cart_garden),
                        contentDescription = "cart",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text= if(inCart) "remove from cart" else "add to cart"
                    )
                }

            }// product != null
        }// column
    }// scaffold
}


