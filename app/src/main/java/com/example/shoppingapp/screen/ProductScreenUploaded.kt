package com.example.shoppingapp.screen

import android.text.Layout
import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.R
import com.example.shoppingapp.Routes
import com.example.shoppingapp.components.RatingBar
import com.example.shoppingapp.viewmodel.ProductsViewModel
import com.example.shoppingapp.viewmodel.ProfileVIewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


@Composable
fun ProductScreenUploaded(id:String , viewModel: ProductsViewModel = viewModel(), profileVIewModel: ProfileVIewModel=viewModel()){

    val roomRepo = SelectedProductsRepository(LocalContext.current)

    var inCart by remember { mutableStateOf(false) }
    var inFav by remember { mutableStateOf(false) }

    // it was not much pain to create a UIState also for single product..
    // so that is just the first in the list, anyways i would first ask to the senior
    val uiState by viewModel.uiProducts.collectAsState()

    val shop by profileVIewModel.profile

    LaunchedEffect(Unit) {
        inCart = roomRepo.getOneCart(id) != null
        inFav = roomRepo.getOneFav(id) != null
        viewModel.getProduct(id)
    }

    // wait until the data of the product is loaded to fetch user
    LaunchedEffect(Unit) {
        var productFetched = false
        while(!productFetched){
            if( uiState.fetching ){
                delay(200)
            }else{
                profileVIewModel.getProfile(uiState.result.first().userId)
                productFetched = true
            }
        }

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
                val images = product.images.split(",")

                val imagesBoxHeight = 256.dp
                // images
                Box(Modifier.fillMaxWidth().height(imagesBoxHeight)){
                    val pagerState = rememberPagerState(initialPage = 0, pageCount = { images.size })
                    HorizontalPager(state = pagerState) { page ->
                        AsyncImage(
                            model = images[page],
                            contentDescription = "image of" + product.title,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier.fillMaxWidth().height(imagesBoxHeight),
                            )
                    }


                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.End
                        ){

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
                                    tint = if (inFav) Color.Red else Color.LightGray,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }
                }


                // USER
                //
                //
                //

                val size = 40.dp

                Row (
                    Modifier.fillMaxWidth().height(size),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // part left: image - title
                    if(shop != null){
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = shop!!.image,
                                contentDescription = shop!!.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(size,size).clip(RoundedCornerShape(100))
                            )
                            Spacer(Modifier.width(size/3))
                            Text(shop!!.name)
                        }
                    }
                    else CircularProgressIndicator(modifier = Modifier.size(size), color = AppStyle.colors.lightBlue)

                    // part right: button or whatelse
                    OutlinedButton(
                        onClick={
                            Routes.navController.navigate(Routes.chat +"/"+ shop?.id +"/"+ shop?.name )
                        }
                    ) {
                        Icon(painter = painterResource(R.drawable.icon_mail), contentDescription = "message shop")
                        Text("Message shop")
                    }


                }


                // title
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleLarge,
                )

                // description
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                )

                // rating
                Text(product.rating)
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
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text= if(inCart) "remove from cart" else "add to cart"
                    )
                }

            }// product != null
        }// column
    }// scaffold
}


