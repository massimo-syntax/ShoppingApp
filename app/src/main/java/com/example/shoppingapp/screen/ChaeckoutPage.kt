package com.example.shoppingapp.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
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
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun CheckoutPage(total: Float) {

    val visible = remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = visible.value,
        enter = scaleIn(
            initialScale = 0.5f
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.6f
        ),
        exit = scaleOut() + fadeOut()
    ) {
        MainContent(total, visible)
    }

    AnimatedVisibility(
        visible = !visible.value,
        enter = scaleIn(
            initialScale = 0.5f
        )
                + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.1f
        ),
        exit = scaleOut() + fadeOut()
    ) {
        PayingContent()
    }


}


@Composable
fun PayingContent() {

    var animState by remember { mutableIntStateOf(0) }

    val paymentProcess = listOf(
        "Payment is processing",
        "Connecting with our servers..",
        "enccrypting your data",
        "Exchanging money throught our proxy banks",
        "Buying some cryptovalue",
        "Selling instantly the cryptovalue",
        "Calculating interest for us",
        "Payment almost done..",
        "Finish! you did pay! "
    )

    LaunchedEffect(Unit) {
        for (i in 0..< paymentProcess.size) {
            delay( Random(seed = i*12345).nextLong(2000, 5000) )
            animState = i
        }
    }

    Box(
        Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            Modifier
                .size(300.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
        ) {
            AnimatedContent(
                targetState = animState,
                label = "animated content",
                transitionSpec = { ( slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut() ).using(
                        // Disable clipping since the faded slide-in/out should
                        // be displayed out of bounds.
                        SizeTransform(clip = false))
                }
            ) { targetState ->
                // Make sure to use `targetCount`, not `count`.
                Column(
                    Modifier.fillMaxWidth().padding(26.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text( paymentProcess[animState],
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = AppStyle.colors.lightBlue,
                        ),
                    )

                }

            }
        }

    }

}






@Composable
fun MainContent(total: Float, visible: MutableState<Boolean>) {


    var checked by remember { mutableStateOf(true) }




    Scaffold(
        topBar = { BackButtonSimpleTopBar("Payment") }
    ) { paddingValues ->

        Column(
            Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
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
            Text(
                "Full Name",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = AppStyle.colors.middleBlue,
                )
            )
            CustomTextField(placeholderText = "Name on the bell")
            Spacer(Modifier.height(20.dp))
            Text(
                "Full Address",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = AppStyle.colors.middleBlue,
                )
            )

            CustomTextField(placeholderText = "Complete address")

            // payment method
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                    visible.value = !visible.value
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
