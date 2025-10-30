package com.example.shoppingapp.screen.mainScreenPages

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.data.model.UiProductWithFieldsFromRoom
import com.example.shoppingapp.data.model.UserSelectedProduct
import com.example.shoppingapp.data.remote.RemoteProduct
import com.example.shoppingapp.repository.RemoteProductsRepository
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.viewmodel.ProductsViewModel
import com.example.shoppingapp.viewmodel.UserSelectedViewModel


@Composable
fun Cart(modifier: Modifier = Modifier , viewModel: UserSelectedViewModel = viewModel() ){

    val uiState = viewModel.uiStateCart.collectAsState()
    val roomRepo = SelectedProductsRepository(LocalContext.current)

    LaunchedEffect(Unit) {
        if(uiState.value.list.isEmpty()){

            viewModel.getAllCart(roomRepo)

        }
        viewModel.getAllCart(roomRepo)

    }


    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ){
        Spacer(Modifier.height(20.dp))

        if(uiState.value.list.isEmpty()){
            Column(
                Modifier.fillMaxSize().background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("no items in the cart")
            }
        }

        if(uiState.value.roomDataLoaded && uiState.value.firebaseDataLoaded){
            SharedElementApp()

            /*
            uiState.value.list.forEach {
                CartItem(it)
            }
            */
             CartScreen()


        }else{
            CircularProgressIndicator()
        }

    }



}

@Composable
fun CartItem(product: UserSelectedProduct , quantity:Int = 0){

    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        AsyncImage(
            model = product.mainPicture,
            contentDescription = product.title,
            modifier = Modifier.size(100.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(product.title)
    }

}




















@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedElementApp() {
    // [START android_compose_animations_shared_element_step1]
    var showDetails by remember {
        mutableStateOf(false)
    }
    SharedTransitionLayout {
        AnimatedContent(
            showDetails,
            label = "basic_transition"
        ) { targetState ->
            if (!targetState) {
                MainContent(
                    onShowDetails = {
                        showDetails = true
                    },
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            } else {
                DetailsContent(
                    onBack = {
                        showDetails = false
                    },
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            }
        }
    }
    // [END android_compose_animations_shared_element_step1]
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
 fun MainContent(
    onShowDetails: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Row(
        // [START_EXCLUDE]
        modifier = Modifier
            .padding(8.dp)
            .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .background(AppStyle.colors.lightBlue, RoundedCornerShape(8.dp))
            .clickable {
                onShowDetails()
            }
            .padding(8.dp)
        // [END_EXCLUDE]
    ) {
        with(sharedTransitionScope) {
            Image(
                painter = painterResource(id = R.drawable.icon_image),
                contentDescription = "Cupcake",
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "image"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            // [START_EXCLUDE]
            Text("Cupcake", fontSize = 21.sp,
                modifier = Modifier.sharedElement(
                rememberSharedContentState(key = "text"),
                animatedVisibilityScope = animatedVisibilityScope
            ))
            // [END_EXCLUDE]
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
 fun DetailsContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Column(
        // [START_EXCLUDE]
        modifier = Modifier
            .padding(top = 0.dp, start = 16.dp, end = 16.dp)
            .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .clickable {
                onBack()
            }
            .padding(8.dp)
        // [END_EXCLUDE]
    ) {
        with(sharedTransitionScope) {
            Image(
                painter = painterResource(id = R.drawable.icon_image),
                contentDescription = "Cupcake",
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "image"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .size(200.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

        // [START_EXCLUDE]

            // [START_EXCLUDE]
            Text("Cupcake", fontSize = 27.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .sharedElement(
                    rememberSharedContentState(key = "text"),
                    animatedVisibilityScope = animatedVisibilityScope
                ))
            // [END_EXCLUDE]
        }


        Text(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur sit amet lobortis velit. " +
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                    " Curabitur sagittis, lectus posuere imperdiet facilisis, nibh massa " +
                    "molestie est, quis dapibus orci ligula non magna. Pellentesque rhoncus " +
                    "hendrerit massa quis ultricies. Curabitur congue ullamcorper leo, at maximus"
        )
        // [END_EXCLUDE]
    }
}


@Composable
fun CartScreen() {
    val products = listOf(
        Product(
            name = "Even better clinical foundation cream",
            brand = "CLINIQUE",
            price = 38.40,
            oldPrice = 38.40,
            imageRes = R.drawable.icon_image // replace with your drawable
        ),
        Product(
            name = "Sculpting touch creamy stick",
            brand = "KIKO MILANO",
            price = 19.00,
            oldPrice = null,
            imageRes = R.drawable.icon_sell // replace with your drawable
        ),
        Product(
            name = "Missha perfect cover",
            brand = "MISSHA",
            price = 12.99,
            oldPrice = 19.95,
            imageRes = R.drawable.icon_save // replace with your drawable
        )
    )

    var quantities by remember { mutableStateOf(List(products.size) { 1 }) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

                ProductCartItem(
                    product = products[0],
                    quantity = quantities[0],
                    onQuantityChange = { newQty ->
                        quantities = quantities.toMutableList().apply { set(0, newQty) }
                    }
                )
                HorizontalDivider(color = Color(0xFFECECEC), thickness = 1.dp)

        ProductCartItem(
            product = products[1],
            quantity = quantities[1],
            onQuantityChange = { newQty ->
                quantities = quantities.toMutableList().apply { set(1, newQty) }
            }
        )
        HorizontalDivider(color = Color(0xFFECECEC), thickness = 1.dp)

        ProductCartItem(
            product = products[2],
            quantity = quantities[2],
            onQuantityChange = { newQty ->
                quantities = quantities.toMutableList().apply { set(2, newQty) }
            }
        )
        HorizontalDivider(color = Color(0xFFECECEC), thickness = 1.dp)



        // Price Summary
        PriceSummary(
            subtotal = 70.39,
            deliveryFee = 5.00,
            discount = 6.04
        )

        // Checkout Bar
        CheckoutBar(
            total = 69.35,
            oldTotal = 75.39
        )
    }
}

data class Product(
    val name: String,
    val brand: String,
    val price: Double,
    val oldPrice: Double?,
    val imageRes: Int
)

@Composable
fun ProductCartItem(
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = product.imageRes),
            contentDescription = product.name,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = 2
            )
            Text(
                text = product.brand,
                fontSize = 12.sp,
                color = Color(0xFF757575)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$${product.price}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                if (product.oldPrice != null && product.oldPrice > product.price) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$${"%.2f".format(product.oldPrice)}",
                        fontSize = 14.sp,
                        color = Color(0xFF757575),
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }
        }
        QuantitySelector(quantity, onQuantityChange)
        IconButton(onClick = { /* remove item */ }) {
            Icon(painter= painterResource(R.drawable.icon_undo), contentDescription = "Remove", tint = Color(0xFF757575))
        }
    }
}

@Composable
fun QuantitySelector(quantity: Int, onQuantityChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(BorderStroke(1.dp, Color(0xFFECECEC)), shape = RoundedCornerShape(4.dp))
    ) {
        IconButton(onClick = { if (quantity > 1) onQuantityChange(quantity - 1) }) {
            Icon(painterResource(R.drawable.icon_back), contentDescription = "Decrease")
        }
        Text(
            text = quantity.toString(),
            modifier = Modifier.width(24.dp),
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
        IconButton(onClick = { onQuantityChange(quantity + 1) }) {
            Icon(painterResource(R.drawable.icon_further), contentDescription = "Increase")
        }
    }
}

@Composable
fun PriceSummary(subtotal: Double, deliveryFee: Double, discount: Double) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        SummaryRow("Subtotal:", "$${"%.2f".format(subtotal)}", bold = true)
        Spacer(modifier = Modifier.height(4.dp))
        SummaryRow("Delivery Fee:", "$${"%.2f".format(deliveryFee)}")
        Spacer(modifier = Modifier.height(4.dp))
        SummaryRow("Discount:", "$${"%.2f".format(discount)}")
    }
}

@Composable
fun SummaryRow(label: String, value: String, bold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color(0xFF757575)
        )
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            color = if (bold) Color.Black else Color(0xFF757575)
        )
    }
}

@Composable
fun CheckoutBar(total: Double, oldTotal: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "$${"%.2f".format(total)}",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            )
            Text(
                text = "$${"%.2f".format(oldTotal)}",
                fontSize = 14.sp,
                color = Color(0xFF757575),
                textDecoration = TextDecoration.LineThrough
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { /* Checkout logic */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier
                .height(48.dp)
                .width(160.dp)
        ) {
            Text(
                text = "Checkout",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}










