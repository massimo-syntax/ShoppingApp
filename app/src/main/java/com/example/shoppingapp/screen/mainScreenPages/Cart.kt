package com.example.shoppingapp.screen.mainScreenPages

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.data.model.UserSelectedProduct
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.viewmodel.UserSelectedViewModel


@Composable
fun Cart(modifier: Modifier = Modifier, viewModel: UserSelectedViewModel = viewModel()) {

    val uiState = viewModel.uiStateCart.collectAsState()
    val roomRepo = SelectedProductsRepository(LocalContext.current)

    // init a list for quantities
    var quantities by rememberSaveable { mutableStateOf<List<Int>>(listOf()) }

    LaunchedEffect(Unit) {
        viewModel.getAllCart(roomRepo)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {

        if (uiState.value.roomDataLoaded && uiState.value.firebaseDataLoaded) {
            if (uiState.value.list.isEmpty()) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("no items in the cart")
                }
            }

            val products = uiState.value.list
            // init quantities
            val q = mutableListOf<Int>()
            products.forEachIndexed { index , _ ->
                if(index < quantities.size){
                    q.add(quantities[index])
                }else{
                    q.add(1)
                }
            }
            quantities = q.toList()

            // calculate subtotal on recomposition
            var subtotal = 0.0f
            products.forEachIndexed { index, product ->
                subtotal += (product.price.toFloat() * quantities[index])
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(products) { index, product ->
                    ProductCartItem(
                        product = product,
                        quantity = quantities[index],
                        onQuantityChange = { newQty ->
                            if (newQty > 0) {
                                quantities = quantities.toMutableList().apply { set(index, newQty) }
                                viewModel.temporaryRestoreToCart(roomRepo, product.id)
                            }
                        },
                        onDelete = {

                            viewModel.temporaryDeleteFromCart(roomRepo, product.id)
                            quantities = quantities.toMutableList().apply { set(index, 0) }

                        },
                        active = product.description != "inactive"
                    )
                    HorizontalDivider(color = Color(0xFFECECEC), thickness = 1.dp)
                }
            }

            val deliveryFee = 5.00f
            val discount = 6.04f
            // Price Summary
            PriceSummary(
                subtotal = subtotal,
                deliveryFee = deliveryFee,
                discount = discount
            )
            // Checkout Bar
            CheckoutBar(
                total = subtotal + deliveryFee - discount,
            )


        }else{
            CircularProgressIndicator()
        }

    }

}


@Composable
fun ProductCartItem(
    product: UserSelectedProduct,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit,
    active: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // image
        AsyncImage(
            model = product.mainPicture,
            contentDescription = product.title,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))
        // title + delete / price + quantity selector
        Column(modifier = Modifier.weight(1f)) {

            // title + delete
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = product.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = if (active) Color.Black else AppStyle.colors.red,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(textDecoration = if (!active) TextDecoration.LineThrough else null),
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onDelete() }) {
                    Icon(
                        painter = painterResource(R.drawable.icon_close),
                        contentDescription = "Remove",
                        tint = Color(0xFF757575)
                    )
                }
            }

            // price + quantity selector
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$${"%.2f".format(product.price.toFloat())}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (active) Color.Black else AppStyle.colors.red,
                    style = TextStyle(textDecoration = if (!active) TextDecoration.LineThrough else null)
                )
                Row {
                    QuantitySelector(quantity, onQuantityChange)
                    Spacer(Modifier.width( (36+12).dp) )
                }

            }
            Spacer(Modifier.height(8.dp))
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
            Icon(painterResource(R.drawable.icon_minus), contentDescription = "Decrease")
        }
        Text(
            text = quantity.toString(),
            modifier = Modifier.width(24.dp),
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
        IconButton(onClick = { onQuantityChange(quantity + 1) }) {
            Icon(painterResource(R.drawable.icon_plus), contentDescription = "Increase")
        }
    }
}

@Composable
fun PriceSummary(subtotal: Float, deliveryFee: Float, discount: Float) {
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
fun CheckoutBar(total: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$${"%.2f".format(total)}",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.Black
        )
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
