package com.example.shoppingapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import com.example.shoppingapp.components.CustomTextField
import com.example.shoppingapp.components.PasswordTextField

@Composable
fun LoginScreen(modifier: Modifier = Modifier){

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text=" login screen"
        )
        Spacer(modifier = Modifier.height(16.dp))

        PasswordTextField()
        CustomTextField()
    }

}


