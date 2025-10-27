package com.example.shoppingapp.screen

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.components.BackButtonSimpleTopBar
import com.example.shoppingapp.components.CustomTextField
import com.example.shoppingapp.repository.MessagesRepository
import com.example.shoppingapp.viewmodel.MessagingViewModel
import com.example.shoppingapp.viewmodel.ProfileVIewModel

@Composable
fun MessagesScreen( idReceiver:String, nameReceiver:String, viewModel: MessagingViewModel = viewModel() , profileViewModel: ProfileVIewModel = viewModel() ) {

    val messages = viewModel.messages

    // text
    val messageText = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // load receiver profile
    val receiver by profileViewModel.profile

    val conversationId = profileViewModel.requestConversation(idReceiver)

    val messagesRepo = MessagesRepository( conversationId )



    LaunchedEffect(Unit) {
        profileViewModel.getProfile(idReceiver)
        viewModel.getAndListenMessages(messagesRepo)
    }

    fun sendMessage(text:String){
        viewModel.sendMessage(messagesRepo, text)
    }


    Scaffold(
        topBar = { BackButtonSimpleTopBar(nameReceiver) } // add image
    ) { scffoldPadding ->

        Column(
            modifier = Modifier
                .imePadding()
                .padding(scffoldPadding)
                .padding(10.dp)
                .fillMaxSize()
        ){
            Column(

            ) {

            }
            LazyColumn (
                Modifier.weight(1f)
            ) {
                items(messages){
                    Text(it.content, modifier = Modifier.padding(6.dp).border(1.dp, AppStyle.colors.lightBlue))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().border(1.dp,AppStyle.colors.lightBlue),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){

                CustomTextField(text = messageText, modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                        // send message
                        sendMessage(messageText.value)
                        focusManager.clearFocus()
                        messageText.value = ""
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_send),
                        contentDescription = "icon send",
                        tint = AppStyle.colors.lightBlue
                    )
                }
            }
        }


    }

}
