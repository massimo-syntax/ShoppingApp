package com.example.shoppingapp.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
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
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.data.model.User
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
                    onClick = {expanded = false},
                    text = { Text("option 1") }
                )
                DropdownMenuItem(
                    onClick = {expanded = false},
                    text = { Text("option 2") }
                )
                DropdownMenuItem(
                    onClick = {expanded = false},
                    text = { Text("option 3") }
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleStandardTopBar(title: String = "Title", dark:Boolean = false) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = if (dark) AppStyle.colors.darkBlule else Color.White,
            scrolledContainerColor = if(dark) AppStyle.colors.darkBlule else Color.White,
            titleContentColor = if (!dark) AppStyle.colors.darkBlule else Color.White,
            subtitleContentColor = if (!dark) AppStyle.colors.darkBlule else Color.White,
            actionIconContentColor = if (!dark) AppStyle.colors.darkBlule else Color.White,
            navigationIconContentColor = if (!dark) AppStyle.colors.darkBlule else Color.White,
            ),
        modifier = Modifier,
        title = { Text(title) },
        actions = {
            IconButton(onClick = {
                Toast.makeText(context, "Nothing", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    painter = painterResource(R.drawable.icon_settings),
                    contentDescription = "menu",
                    tint = Color.White
                )
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    painter = painterResource(R.drawable.icon_menu),
                    contentDescription = "menu",
                    tint = Color.White
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    onClick = {expanded = false},
                    text = { Text("option 1") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.icon_menu),
                            contentDescription = "menu",
                            tint = AppStyle.colors.darkBlule
                        )
                    }
                )
                DropdownMenuItem(
                    onClick = {expanded = false},
                    text = { Text("option 2") }
                )
                DropdownMenuItem(
                    onClick = {
                        Firebase.auth.signOut()
                              },
                    text = { Text("Sign Out") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.icon_profile),
                            contentDescription = "menu",
                            tint = AppStyle.colors.darkBlule
                        )
                    }
                )
            }
        },
    )
}















// NOT POSSIBLE TO DO: NO TEXTFIELD POPPING UP
/*
    - when the view is recomposed, every combination of focus-requester-manager,  keyboard controller
    do not prevent the keyboard bouncing.. get hidden, then again appears for some ms then disappeares again,
    every time..
    I wanted to do like Whatsapp, which is kind of opening another fragment even hiding the bottom bar
    when on search..
*/
// THAT WAS ALSO JUST EXPERIMENTAL
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHouse(textState: TextFieldState) {
    var active by remember {mutableStateOf(false)}
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    keyboardController?.hide()

    TopAppBar(
        modifier = Modifier.background(color = Color.Transparent),
        title = {
            if(active) SearchTextField(textState , focusRequester)
            else Text("title")
                },
        actions = {
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = {

                if(active){
                    //focusRequester.requestFocus()
                    textState.setTextAndPlaceCursorAtEnd("")
                    //keyboardController?.show()
                }else{
                    //textState.clearText()
                    //textState.setTextAndPlaceCursorAtEnd("")
                    //focusManager.clearFocus()
                   // keyboardController?.hide()
                }
                active = !active
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

*/
