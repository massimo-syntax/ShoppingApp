package com.example.shoppingapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R





@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholderText: String = "Placeholder",
    fontSize: TextUnit = 16.sp,
    text:String = "",
    valueChange: (String)->Unit = {},
    singleLine:Boolean = true,
    keyboard: KeyboardType = KeyboardType.Unspecified
) {
    BasicTextField(modifier = modifier
        .fillMaxWidth(),
        value = text,
        keyboardOptions = KeyboardOptions(keyboardType = keyboard),
        onValueChange = {
            //text.value = it
            valueChange(it)
        },
        singleLine = singleLine,
        cursorBrush = SolidColor(AppStyle.colors.darkBlule),
        textStyle = LocalTextStyle.current.copy(
            color = AppStyle.colors.darkBlule,
            fontSize = fontSize
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier
                    .border(1.dp, AppStyle.colors.darkBlule, shape = RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical= 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f).padding(8.dp)) {
                    if (text.isEmpty()) Text(
                        placeholderText,
                        style = LocalTextStyle.current.copy(
                            color = Color.LightGray,
                            fontSize = fontSize
                        )
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
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


//
//
//  THESE ARE JUST EXPERIMENTAL..
//

/*
@Composable
fun CustomTextField__1(textFieldState: TextFieldState){

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
fun SearchTextField(textFieldState: TextFieldState , focusRequester: FocusRequester){
    //val state = remember { TextFieldState() }
    BasicTextField(
        state = textFieldState,
        modifier = Modifier
            .focusRequester(focusRequester)
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

*/

