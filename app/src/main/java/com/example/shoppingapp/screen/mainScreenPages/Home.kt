package com.example.shoppingapp.screen.mainScreenPages

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.components.CustomTextField

import com.example.shoppingapp.data.remote.Product



@Composable
fun Home(modifier: Modifier = Modifier) {

    // get all products
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    LaunchedEffect(Unit) {
        // viewmodel fetching products
    }


    Column(
        modifier = modifier
            .border(5.dp, Color.Green)
            .fillMaxSize(),
        //horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            text = "NOTHING",
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),
        )

        Spacer(modifier = Modifier.height(20.dp))

        CircularProgressIndicator(
            modifier = Modifier.width(0.dp),
            color = Color.DarkGray
        )




        Button(onClick = {
        },
            shape = RoundedCornerShape(percent = 5),
            colors = ButtonColors(
                containerColor = AppStyle.colors.lightBlue,
                contentColor = Color.Black,
                disabledContainerColor = AppStyle.colors.lightBlue,
                disabledContentColor = AppStyle.colors.lightBlue
            )) {
            Text("button")
        }


        AsyncImage(
            model ="https://avatars.githubusercontent.com/u/148047335?v=4",
            contentDescription = "hello"
        )




    }


}




