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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.shoppingapp.components.BackButtonSimpleTopBar
import com.example.shoppingapp.data.model.UiProductWithFieldsFromRoom
import com.example.shoppingapp.repository.RemoteProductsRepository
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.R
import com.example.shoppingapp.components.RatingBar
import kotlinx.coroutines.runBlocking
import kotlin.random.Random


@Composable
fun ProductScreen(id:String){



    val roomRepo = SelectedProductsRepository(LocalContext.current)
    val remoteRepo = RemoteProductsRepository()


    var inCart by remember { mutableStateOf(false) }
    var inFav by remember { mutableStateOf(false) }



    var _product by remember { mutableStateOf<UiProductWithFieldsFromRoom?>(null) }


    LaunchedEffect(Unit) {

        val remoteProduct = remoteRepo.getProduct(id)

        val fav = roomRepo.getOneFav(remoteProduct.id)
        val cart = roomRepo.getOneCart(remoteProduct.id)

        inCart = cart != null && cart.productId == remoteProduct.id
        inFav = fav != null && fav.productId == remoteProduct.id

        val rating = String.format("%.2f" , Random.nextFloat() )

        val uiProduct = UiProductWithFieldsFromRoom(
            id = remoteProduct.id,
            title = remoteProduct.title,
            description = remoteProduct.description,
            images = remoteProduct.image,
            price = remoteProduct.image,
            category = remoteProduct.category,
            rating = rating,
            cart = inCart,
            fav = inFav
        )

        _product = uiProduct

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
            if (_product != null) {

                val product = _product!!

                AsyncImage(product.images, contentDescription = "image of" + product.title)

                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
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

                RatingBar(3.5.toFloat())

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


