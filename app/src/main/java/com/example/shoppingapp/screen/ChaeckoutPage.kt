package com.example.shoppingapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.Routes
import com.example.shoppingapp.components.BackButtonSimpleTopBar
import com.example.shoppingapp.components.CustomTextField
import com.example.shoppingapp.components.SimpleStandardTopBar


@Composable
fun CheckoutPage(total:Float){

    var checked by mutableStateOf(true)

    Scaffold(
        topBar = { BackButtonSimpleTopBar("Payment") }
    ) { paddingValues ->

        Column(
            Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ){
            // products
            Column {
                Routes.checkoutPayload.forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${it.quantity}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = AppStyle.colors.middleBlue
                        )
                        Spacer(Modifier.width(10.dp))
                        AsyncImage(
                            model = it.image,
                            contentDescription = it.title,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            it.title,
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = AppStyle.colors.darkBlule,
                                fontWeight = FontWeight.Medium
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Clip
                        )
                    }
                }

                // total
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center

                ) {
                    Text(
                        text = "€ ${"%.2f".format(total)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = AppStyle.colors.green
                    )
                }
            }

            // address textfields
            Text("Full Name",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = AppStyle.colors.middleBlue,
                )
            )
            CustomTextField(placeholderText = "Name on the bell")
            Spacer(Modifier.height(20.dp))
            Text("Full Address",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = AppStyle.colors.middleBlue,
                )
            )

            CustomTextField(placeholderText = "Complete address")

            // payment method
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        painter = painterResource(R.drawable.adaptivecarticon_foreground),
                        contentDescription = "cart image",
                        tint = AppStyle.colors.green
                    )
                    Text(
                        "Pay with PayCartApp"
                    )
                }
                Checkbox(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }

            //tton
            Button(
                onClick = {
                    // start nice animation
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppStyle.colors.darkBlule),
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "click here",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

        }

    }

}
