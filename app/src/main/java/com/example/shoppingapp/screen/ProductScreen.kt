package com.example.shoppingapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shoppingapp.components.BackButtonSimpleTopBar

@Composable
fun ProductScreen(id:String){

    Scaffold(
        topBar = {
            BackButtonSimpleTopBar("Product", false)
        },
        modifier = Modifier.fillMaxSize(),
    ){ paddingScaffold ->
        Column(
            modifier = Modifier.padding(paddingScaffold).fillMaxSize(),
        ) {
            Spacer(Modifier.height(20.dp))
            Text(id)
        }

    }


}