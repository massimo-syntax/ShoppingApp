package com.example.shoppingapp.screen
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shoppingapp.R
import com.example.shoppingapp.Routes
import com.example.shoppingapp.viewmodel.AuthViewModel


@Composable
fun LoginScreen( navController: NavController, authViewModel: AuthViewModel = viewModel()){

    val context = LocalContext.current
    fun toast(message:Any?){
        if(message == null)Toast.makeText(context, "toast not working, MESSAGE IS NULL", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show()
    }

    var email by remember{mutableStateOf("")}
    var password by remember { mutableStateOf("") }
    var loading by remember {mutableStateOf(false)}

    Column (
        modifier = Modifier
            .imePadding()
            .verticalScroll(rememberScrollState())
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
                if(email.isEmpty() || password.isEmpty()){
                    toast("All fields are required")
                    return@Button
                }
                loading = true
                authViewModel.login(email, password){ success, message ->
                    if(success){
                        //redirect user
                        navController.navigate(Routes.main){
                            // pop [auth][signup] backstack, first in backstack is main now
                            popUpTo(Routes.auth){inclusive = true}
                        }
                        loading = false
                    }else{
                        toast(message)
                        loading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            enabled = !loading
        ) {
            if(loading)
                CircularProgressIndicator(
                    modifier = Modifier.width(36.dp),
                    color = Color.DarkGray
                )
            else
                Text(
                    text = "Login",
                    fontSize = 22.sp
                )
        }



    }

}


