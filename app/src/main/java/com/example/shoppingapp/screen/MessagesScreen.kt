package com.example.shoppingapp.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.components.CustomTextField
import com.example.shoppingapp.components.MessagesTopBar
import com.example.shoppingapp.data.model.Message
import com.example.shoppingapp.viewmodel.MessagesViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessagesScreen(
    idReceiver: String,
    viewModel: MessagesViewModel = viewModel(),
) {

    // text field
    var messageText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current


    val receiverProfile = viewModel.receiverProfile
    val myProfile = viewModel.myProfile
    val messages = viewModel.messages

    LaunchedEffect(Unit) {
        viewModel.getUserProfilesAndRequestConversation(idReceiver)
    }

    // content
    Scaffold(
        topBar = { MessagesTopBar(receiverProfile.image, receiverProfile.name )}
    ) { scffoldPadding ->
        if (myProfile.id.isEmpty()) return@Scaffold
        Column(
            modifier = Modifier
                .imePadding()
                .padding(scffoldPadding)
                .padding(10.dp)
                .fillMaxSize()
        ) {

            LazyColumn(
                Modifier.weight(1f)
            ) {
                items(messages) {
                    // print day when different
                    if (it.id == "LABEL"){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            Text(
                                text = it.content,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.LightGray,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        }
                    }else{
                        if(myProfile.id == it.senderId)
                            MessageSenderMe(it)
                        else
                            MessageSenderOther(it)
                    }

                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    text = messageText,
                    valueChange = {messageText = it},
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        // send message
                        if(messageText.isEmpty())return@IconButton
                        viewModel.sendMessage( messageText )
                        focusManager.clearFocus()
                        messageText = ""
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_send),
                        contentDescription = "icon send",
                        tint = AppStyle.colors.lightBlue,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

    }

}

@Composable
fun MessageSenderMe(message: Message){
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.End
    ) {

        Row(
            modifier = Modifier.width(260.dp),
            horizontalArrangement = Arrangement.End
        ) {

            Column(
                Modifier
                    .border(2.dp, color = AppStyle.colors.middleBlue, shape = RoundedCornerShape(10.dp,10.dp,0.dp,10.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = message.content,
                    color = AppStyle.colors.darkBlue
                )
                // datetime
                val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                val date = Date(message.timestamp)
                val text = sdf.format(date)
                Text(
                    text,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Normal
                    )
                )
            }

        }

    }

}

@Composable
fun MessageSenderOther(message: Message){
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.Start
    ) {

        Row(
            modifier = Modifier.width(260.dp),
            horizontalArrangement = Arrangement.Start
        ) {

            Column(
                Modifier
                    .border(2.dp, color = AppStyle.colors.lightBlue, shape = RoundedCornerShape(10.dp,10.dp,10.dp,0.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
            ) {
                Text(
                    text = message.content,
                    color = AppStyle.colors.darkBlue
                )
                // datetime
                val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                val date = Date(message.timestamp)
                val text = sdf.format(date)
                Text(
                    text,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Normal
                    ),
                )
            }

        }

    }
}

