package com.example.shoppingapp.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.RectangleShape

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
import com.example.shoppingapp.R

import com.example.shoppingapp.viewmodel.AuthViewModel

@Composable
fun SignupScreen(modifier: Modifier = Modifier , authViewModel: AuthViewModel = viewModel() ){

    var email by remember{mutableStateOf("")}
    var name by remember{mutableStateOf("")}
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }

    var signupLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column (
        modifier = modifier
            .imePadding()
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ){
        Text(
            text = "Signup here",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Create your account",
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
                .border(border = BorderStroke(2.dp, Color.Red), shape = RectangleShape)

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
            value = name,
            onValueChange = {name = it},
            label = {Text("Name")},
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
        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = password2,
            onValueChange = {password2 = it},
            label = {Text("Repeat password")},
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
        )
        Spacer(Modifier.height(15.dp))

        Button(
            onClick = {
                if(email.isEmpty()||name.isEmpty()||password.isEmpty()||password2.isEmpty()){
                    Toast.makeText(context, "all fields are required",Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if(password == password2){
                    signupLoading = true
                    authViewModel.signup(email, name, password){ successful, message ->
                        if(successful){
                            Toast.makeText(context,"User created successfully", Toast.LENGTH_SHORT).show()
                            signupLoading = false
                        }else{
                            Toast.makeText(context, message?:"creating user failed", Toast.LENGTH_SHORT).show()
                            signupLoading = false
                        }
                    }
                }else{ // password do not match
                    Toast.makeText(context,"Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            enabled = !signupLoading
            ) {

            if(signupLoading)
                CircularProgressIndicator(
                    modifier = Modifier.padding(4.dp),
                    color = Color.DarkGray
                )
            else
                Text(
                    text = "Signup",
                    fontSize = 22.sp
                )
        }
    }

}