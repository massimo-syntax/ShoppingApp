package com.example.shoppingapp.screen.mainScreenPages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ChipColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.Category
import com.example.shoppingapp.R
import com.example.shoppingapp.Routes
import com.example.shoppingapp.components.EndlessPager
import com.example.shoppingapp.components.MainProductCard
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.viewmodel.ProductsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    }

    val spaceSmall = 10.dp
    val modifierPaddingSmall = Modifier.padding(spaceSmall)


    Column(modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
    ) {
        // Pager
        EndlessPager(imagesForBanner.shuffled())

        //Welconimg Titile
        Text("New products",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = AppStyle.colors.lightBlue,
            ),
            modifier = Modifier.padding(horizontal = spaceSmall).padding(top=spaceSmall)
        )

        Text("Perfect to buy now! Dont wait, buy fast and easy directly here!",
            style = TextStyle(color = AppStyle.colors.middleBlue , fontWeight = FontWeight.Medium ),
            modifier = modifierPaddingSmall
        )



        if(!uiProducts.fetching && uiProducts.result.isNotEmpty()){
            LazyRow(
                modifier = modifierPaddingSmall
            ) {
                val products = uiProducts.result
                itemsIndexed(products ){ index , product ->
                    // todo add product card
                    MainProductCard(
                        modifier = Modifier.padding(end=spaceSmall),
                        product = product,
                        isInFav = product.fav,
                        onToggleFav = {
                            viewModel.toggleFav( roomRepo, product )
                        }
                    )
                }
            }
        }
        else
            Row(
                Modifier.height(225.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp)
                )
            }


        // Categories list title
        Row(
            modifier = modifierPaddingSmall.fillMaxWidth(),
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
                        Routes.navController.navigate( Routes.category +"/"+ it )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors().copy(
                        containerColor = AppStyle.colors.darkBlule,
                        labelColor = Color.White
                    ),
                )
            }
        }

        Text("Find other good products !",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = AppStyle.colors.middleBlue,
            ),
            modifier = Modifier.padding(horizontal = spaceSmall).padding(top=spaceSmall)
        )
        Text("Here you can choose the product that you like most! you can even like it before you buy!",
            style = TextStyle(color = AppStyle.colors.darkBlule , fontWeight = FontWeight.Medium ),
            modifier = modifierPaddingSmall
        )

        LazyRow(
            modifier = modifierPaddingSmall
        ) {
            val products = uiProducts.result
            itemsIndexed(products.shuffled() ){ index , product ->
                // todo add product card
                MainProductCard(
                    modifier = Modifier.padding(end=spaceSmall),
                    product = product,
                    isInFav = product.fav,
                    onToggleFav = {
                        viewModel.toggleFav( roomRepo, product )
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













