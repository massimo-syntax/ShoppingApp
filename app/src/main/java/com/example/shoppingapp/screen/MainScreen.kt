package com.example.shoppingapp.screen

import android.R
import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun MainScreen(modifier: Modifier = Modifier){

    TopBar()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Main screen")
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    TopAppBar(
        title = { if(false)Text("Logout")else null },
        actions = {
            IconButton(onClick = {
                Firebase.auth.signOut()
                Toast.makeText(context, "SIGNED OUT", Toast.LENGTH_SHORT).show()
            }) {
                Image(
                    painter = painterResource(com.example.shoppingapp.R.drawable.ic_launcher_foreground),
                    contentDescription = "cart icon"
                )
            }
            IconButton(onClick = { expanded = !expanded }) {
                Image(
                    painter = painterResource(com.example.shoppingapp.R.drawable.ic_launcher_background),
                    contentDescription = "cart icon"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    onClick = {},
                    text = { Text("option 1") }
                )
                DropdownMenuItem(
                    onClick = {},
                    text = { Text("option 2") }
                )
                DropdownMenuItem(
                    onClick = {},
                    text = { Text("option 3") }
                )
            }
        },
    )
}