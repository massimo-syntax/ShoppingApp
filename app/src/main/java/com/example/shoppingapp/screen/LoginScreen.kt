package com.example.shoppingapp.screen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppingapp.R

@Composable
fun LoginScreen(modifier: Modifier = Modifier){

    var email by remember{mutableStateOf("")}
    var password by remember { mutableStateOf("") }

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Login",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Login into your account",
            style = TextStyle(
                fontSize = 22.sp,
            )
        )
        Spacer(Modifier.height(20.dp))
        Image(
            painter = painterResource(R.drawable.adaptivecarticon_foreground),
            contentDescription = "cart icon",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)

        )
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = {Text("Email")},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))


        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = {Text("Password")},
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(15.dp))

        Button(
            onClick = {
                if(email.isEmpty() || password.isEmpty())
                    email = "something empty"
                else
                    email = "nice email"
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



    }

}


