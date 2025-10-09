package com.example.shoppingapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppingapp.R

@Composable
fun CustomTextField(textFieldState: TextFieldState){

    //val state = remember { TextFieldState() }

    BasicTextField(
        state = textFieldState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
            .padding(2.dp),
        textStyle = TextStyle(fontSize = 18.sp),
        decorator = { innerTextField ->
            Box(modifier = Modifier.fillMaxWidth().border(1.dp,Color.Green)) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp, end = 48.dp)
                        .border(1.dp, Color.Red, RectangleShape)
                ) {
                    innerTextField()
                }
                Icon(
                    painter = painterResource(R.drawable.adaptivecarticon_foreground),
                    contentDescription = "textfield icon",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .requiredSize(48.dp)
                        .padding(2.dp)
                        .border(1.dp,Color.Cyan)
                )
            }
        }
    )
}




@Composable
fun SearchTextField(textFieldState: TextFieldState){
    //val state = remember { TextFieldState() }
    BasicTextField(
        state = textFieldState,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(Color.LightGray)
            .border(1.dp, Color.Blue, RoundedCornerShape(16.dp)),
        textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = Color.Green
        ),
        decorator = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp, end = 16.dp)
                        .border(1.dp, Color.Red, RectangleShape)
                ) {
                    innerTextField()
                }
            }
        }
    )
}


@Composable
fun PasswordTextField() {
    val state = remember { TextFieldState() }
    var showPassword by remember { mutableStateOf(false) }

    BasicSecureTextField(
        state = state,
        textObfuscationMode =
            if (showPassword) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.RevealLastTyped
            },
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
            .padding(2.dp),
        textStyle = TextStyle(fontSize = 18.sp),
        decorator = { innerTextField ->
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp, end = 48.dp)
                ) {
                    innerTextField()
                }
                Icon(
                    painter =
                        if (showPassword) {
                            //Icons.Filled.Visibility
                            painterResource( R.drawable.ic_launcher_foreground)
                        } else {
                            //Icons.Filled.VisibilityOff
                            painterResource( R.drawable.ic_launcher_background)
                        },
                    contentDescription = "Toggle password visibility",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .requiredSize(48.dp)
                        .padding(2.dp)
                        .clickable { showPassword = !showPassword }
                )
            }
        }
    )
}