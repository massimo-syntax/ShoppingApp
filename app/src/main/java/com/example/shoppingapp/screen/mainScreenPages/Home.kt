package com.example.shoppingapp.screen.mainScreenPages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.Category
import com.example.shoppingapp.R
import com.example.shoppingapp.Routes
import com.example.shoppingapp.components.EndlessPager
import com.example.shoppingapp.data.model.UiProductWithFieldsFromRoom
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.viewmodel.ProductsViewModel
import kotlinx.coroutines.runBlocking


@Composable
fun Home(modifier: Modifier = Modifier ,  viewModel: ProductsViewModel = viewModel()) {


    val context = LocalContext.current
    fun toast(message:Any){
        Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show()
    }

    val categories = Category.entries.map { it.enam }

    // get cart and fav from ROOM
    val roomRepo = SelectedProductsRepository(context)

    // products from Firebase
    val uiProducts by viewModel.uiProducts.collectAsState()


    val imagesForBanner = listOf(
        "https://thumbs.wbm.im/pw/small/92c2fbe5257623fe6d57a3399ffcd286.jpg",
        "https://thumbs.wbm.im/pw/medium/5a654aff4ee2fd3d9f061d6d00ab5708.jpg",
        "https://t3.ftcdn.net/jpg/02/26/54/32/360_F_226543201_ZtKNzyJqxGdW5Lc1louWUd3euRmbjDh9.jpg",
        "https://t3.ftcdn.net/jpg/02/28/87/62/360_F_228876249_pfyLC3Kn976HOyHgzlQ5L2oH4CcYYMyD.jpg",
        "https://thumbs.wbm.im/pw/medium/6910be32cd99b7a5a6d6e3a094cafa1d.jpg",
        "https://thumbs.wbm.im/pw/medium/5f301e483793e9fa9d52ab20ebd377ba.jpg",
        "https://thumbs.wbm.im/pw/medium/fb3667c9e2b6e58776d9ec5ceb8ce6f4.jpg",
        "https://thumbs.wbm.im/pw/medium/29195c0bd1049e7730f3c3d9b0a10c2d.jpg",
        "https://thumbs.wbm.im/pw/medium/a01c7657a3ebdf0cc8626433771ca716.jpg",
        "https://thumbs.wbm.im/pw/medium/9ee418a4b66688ceee3278d579fdb937.jpg",
        "https://thumbs.wbm.im/pw/medium/d162945b80040f050d8541c09f9e85e2.jpg",
        )


    LaunchedEffect(Unit) {
        viewModel.getAllProducts(roomRepo)
        //productsViewModel.getCategory(Category.ELECTRONICS)
    }


    Column(modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
    ) {
        // Pager
        EndlessPager(imagesForBanner.shuffled())

        //Welconimg Titile
        Text("New products",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = AppStyle.colors.middleBlue,
            )
        )
        Text("Perfect to buy now! Dont wait, buy fast and easy directly here!",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = AppStyle.colors.middleBlue,
            )
        )

        if(!uiProducts.fetching && uiProducts.result.isNotEmpty()){
            LazyRow {
                val products = uiProducts.result
                itemsIndexed(products){ index , product ->
                    // todo add product card
                    MainProductCard(
                        product = product,
                        isInFav = product.fav,
                        onToggleFav = {
                            runBlocking {
                                roomRepo.toggleFav(product.id)
                            }
                            product.fav = !product.fav
                            viewModel.updateList(index,product)
                        }
                    )
                }
            }
        }
        else
            CircularProgressIndicator()

        // Categories list title
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text("Trending categories",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = AppStyle.colors.middleBlue,
                )
            )
            //
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .clickable(onClick = {
                    // navigate to all categories page
                    })
            ){
                Text("All categories",
                    style = TextStyle(color = AppStyle.colors.lightBlue)
                )
                Icon(
                    painter = painterResource(R.drawable.icon_further),
                    contentDescription = "icon further",
                    tint = AppStyle.colors.lightBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // categories in list
        LazyRow {
            items(categories ){
                SuggestionChip(
                    modifier=Modifier.padding(6.dp),
                    label = { Text(it) },
                    onClick = {

                    }
                )
            }
        }





        Spacer(Modifier.height(36.dp))



        Text("Perfect to buy now", style = TextStyle(color = AppStyle.colors.middleBlue , fontWeight = FontWeight.Bold ))
        Text("Perfect to buy now", style = TextStyle(color = AppStyle.colors.middleBlue , fontWeight = FontWeight.Medium ))
        Text("Perfect to buy now", style = TextStyle(color = AppStyle.colors.middleBlue , fontWeight = FontWeight.Normal ))
        Text("Perfect to buy now", style = TextStyle(color = AppStyle.colors.middleBlue , fontWeight = FontWeight.Thin ))
        Text("Perfect to buy now", style = TextStyle(color = AppStyle.colors.middleBlue , fontWeight = FontWeight.Light ))
        Text("Perfect to buy now", style = TextStyle(color = AppStyle.colors.middleBlue , fontWeight = FontWeight.ExtraLight ))



    }

}





@Composable
fun MainProductCard(
    product: UiProductWithFieldsFromRoom,
    onToggleFav: () -> Unit,
    isInFav:Boolean,
) {

        Column(
            modifier = Modifier
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

                /*
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    // see button
                    OutlinedButton(
                        onClick = {
                            Routes.navController.navigate(Routes.product+"/"+product.id)
                        },
                        modifier = Modifier
                            .height(36.dp)
                            .width(100.dp)
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
                }*/
            }
        }

}












