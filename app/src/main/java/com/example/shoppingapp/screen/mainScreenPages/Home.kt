package com.example.shoppingapp.screen.mainScreenPages

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

        "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%2Fid%2FOIF.xQ0b4dehSn5BCsVYtwymtw%3Fpid%3DApi&f=1&ipt=71b44b97ec6a8fb2f94c4ac43e0a9c56ebc0ea475f95dd030d947e967e00145c&ipo=images",
        "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%2Fid%2FOIP.hh2LFcptiqicfhOmitj_5wHaFj%3Fcb%3Ducfimg2%26pid%3DApi%26ucfimg%3D1&f=1&ipt=15a88cbd1cc40eb1c3f60dccf90405e2e98a48e071ddf6f33386a45e819e88fc&ipo=images",
        "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%2Fid%2FOIP.eho3h1am0_qVdU_n_ppI3AHaE7%3Fcb%3Ducfimgc2%26pid%3DApi&f=1&ipt=92aa694ee6798742d9a2d1f4c0eef5b237c0862240d877af330f20b6f0ef7a95&ipo=images",
        "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%2Fid%2FOIP.FDmuJRfi4-dXtPjxtKfqMwHaE7%3Fcb%3Ducfimgc2%26pid%3DApi&f=1&ipt=9f1bdf43357f363946d30cc87d2062aac3eeb249d61a5628439ab2e4147daff2&ipo=images",
        "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%2Fid%2FOIP.pQDcBamA1j_aBc0Tp2JAAQHaE7%3Fcb%3Ducfimgc2%26pid%3DApi&f=1&ipt=8f7aa7efd821f1650a7a6d9d02d7461444b4f94ac41945b1878277d9602f080e&ipo=images"
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
                color = AppStyle.colors.middleBlue,
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
                        containerColor = AppStyle.colors.darkBlue,
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
            style = TextStyle(color = AppStyle.colors.darkBlue , fontWeight = FontWeight.Medium ),
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













