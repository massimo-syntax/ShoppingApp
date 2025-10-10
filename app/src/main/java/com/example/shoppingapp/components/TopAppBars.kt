package com.example.shoppingapp.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.shoppingapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHome() {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    TopAppBar(
        modifier = Modifier.border(5.dp, Color.Cyan),
        title = { if(false)Text("Logout")else null },
        actions = {
            IconButton(onClick = {
                Firebase.auth.signOut()
                Toast.makeText(context, "SIGNED OUT", Toast.LENGTH_SHORT).show()
            }) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "cart icon"
                )
            }
            IconButton(onClick = { expanded = !expanded }) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHouse(textState: TextFieldState) {
    var active by remember {mutableStateOf(false)}
    val focusRequester = remember { FocusRequester() }
    //val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    TopAppBar(
        modifier = Modifier.background(color = Color.Transparent),
        title = {
            SearchTextField(textState , focusRequester)
                },
        actions = {
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = {
                active = !active
                if(active){

                    focusRequester.requestFocus()
                    textState.setTextAndPlaceCursorAtEnd("")
                    keyboardController?.show()

                }else{
                    //textState.clearText()
                    textState.setTextAndPlaceCursorAtEnd("")
                    //focusManager.clearFocus()
                    keyboardController?.hide()

                }
            }) {
                if(active)
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "cart icon",
                )
                else
                    Image(
                        painter = painterResource(R.drawable.adaptivecarticon_foreground),
                        contentDescription = "cart icon",
                    )
            }

            IconButton(onClick = {}) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "cart icon",
                )
            }


        },
    )
}