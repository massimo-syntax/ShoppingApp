package com.example.shoppingapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.shoppingapp.Routes
import com.example.shoppingapp.R

@Composable
fun AuthScreen(modifier: Modifier = Modifier, navController: NavHostController){

    Column(
        modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.adaptivecarticon_foreground),
            contentDescription = "cart icon",
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
            )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Buy more, be more happy!",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "The more you buy, the happier you will be",
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate(Routes.login)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
            ) {
            Text(
                text = "Login",
                fontSize = 22.sp
            )
        }
        Spacer(Modifier.height(20.dp))

        OutlinedButton(
            onClick = {
                navController.navigate(Routes.signup)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(
                text = "Signup",
                fontSize = 22.sp
            )
        }

    }
}