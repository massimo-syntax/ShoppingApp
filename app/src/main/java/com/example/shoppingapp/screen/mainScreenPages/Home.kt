package com.example.shoppingapp.screen.mainScreenPages

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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

        var textState by remember { mutableStateOf("") }
        val maxLength = 110
        val lightBlue = Color(0xffd8e6ff)
        val blue = Color(0xff76a9ff)

        val theother = AppStyle.colors.lightBlue


        Text(
            text = "Caption",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            textAlign = TextAlign.Start,
            color = blue
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textState,
            colors = TextFieldDefaults.colors (
                cursorColor = Color.Black,
                disabledLabelColor = lightBlue,
                focusedContainerColor = lightBlue,
                unfocusedContainerColor = lightBlue,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),

            onValueChange = {
                if (it.length <= maxLength) textState = it
            },
            shape = RoundedCornerShape(percent = 100),
            singleLine = true,
            trailingIcon = {
                if (textState.isNotEmpty()) {
                    IconButton(onClick = { textState = "" }) {
                        Icon(
                            painterResource(R.drawable.adaptivecarticon_foreground),
                            contentDescription = null
                        )
                    }
                }
            }
        )

        Text(
            text = "${textState.length} / $maxLength",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            textAlign = TextAlign.End,
            color = blue
        )


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


        val focusManager = LocalFocusManager.current


        Row (Modifier.padding(32.dp)) {

            CustomTextField(
                modifier = Modifier.padding(6.dp),
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.height(36.dp).width(36.dp),
                        onClick = {
                            focusManager.clearFocus()
                        }) {
                        Icon(painterResource(R.drawable.adaptivecarticon_foreground) , contentDescription = "hello")
                    }
                }

            )

        }

        Button(onClick = {
            focusManager.clearFocus()
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




