package com.example.shoppingapp.screen

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.components.BackButtonSimpleTopBar
import com.example.shoppingapp.components.CustomTextField
import com.example.shoppingapp.components.MessagesTopBar
import com.example.shoppingapp.data.model.User
import com.example.shoppingapp.repository.MessagesRepository
import com.example.shoppingapp.viewmodel.MessagesViewModel
import com.example.shoppingapp.viewmodel.ProfileVIewModel
import kotlinx.coroutines.delay

@Composable
fun MessagesScreen(
    idReceiver: String,
    nameReceiver: String,
    viewModel: MessagesViewModel = viewModel(),
    profileViewModel: ProfileVIewModel = viewModel()
) {

    val ctx = LocalContext.current
    fun tst(any: Any?) {
        Toast.makeText(ctx, any.toString(), Toast.LENGTH_SHORT).show()
    }

    val messages = viewModel.messages

    // text
    val messageText = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // load receiver profile
    var profileCollect by profileViewModel.profile

    var receiverProfile by remember { mutableStateOf<User?>(null) }
    var myProfile by remember { mutableStateOf<User?>(null) }

    var pleaseRecomposeLastTime by remember { mutableStateOf(false) }

    var messagesRepo by remember { mutableStateOf<MessagesRepository?>(null) }

    val mMediaPlayer = MediaPlayer.create(LocalContext.current, R.raw.huddle)


    fun recomposeLastTime() {
        pleaseRecomposeLastTime = true
    }

    LaunchedEffect(Unit) {

        profileViewModel.getProfile()

        // wait until profile is here
        while (profileCollect == null) {
            delay(200)
        }
        myProfile = profileCollect!!.copy()

        // has to be first myProfile to load, so profileConnect is going to update
        // otherwise the thread continues, and whatever, is not executing those next very crucial lines
        var conversationId = ""
        conversationId = profileViewModel.requestConversation(idReceiver)
        messagesRepo = MessagesRepository(conversationId)

        // then load a new profile to change profile collect
        profileViewModel.getProfile(idReceiver)
        while (receiverProfile == null) {
            delay(200)
        }
        // what is strange is that now receiver profile is not updated yet at last recomposition
        receiverProfile = profileCollect!!.copy()

        // so we ask for recomposition in the composable itself..
        // checking for the repository that is already been updated correctly, now..
        // profileCollect has now also the receiver profile, while receiverProfile remains null
        // even after receomposelasttime(), which is working for the topBar, or it blinks the ither name first B)

        // now i know that Launched effect is not always in series, and bringing around values of state is not always recomposing
        // just could be pretty in the recruiting process get to hire considering my effort, that with asking a senior could have taken 10 minutes
        // instead of hours and hours. anyways firestore is also not really good for messaging.

    }



    fun sendMessage(messagesRepo: MessagesRepository, text: String) {
        viewModel.sendMessage(messagesRepo, text)
    }


    Scaffold(
        topBar = {
            if (pleaseRecomposeLastTime) {
                MessagesTopBar(profileCollect?.image ?: "", profileCollect?.name ?: "")
            }
        }
    ) { scffoldPadding ->

        Column(
            modifier = Modifier
                .imePadding()
                .padding(scffoldPadding)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            if (messagesRepo != null) {
                viewModel.getAndListenMessages(messagesRepo!!)
                // this is going to update the topBar
                recomposeLastTime()
            }

            LazyColumn(
                Modifier.weight(1f)
            ) {
                items(messages) {
                    Text(
                        it.content,
                        modifier = Modifier
                            .padding(6.dp)
                            .border(1.dp, AppStyle.colors.lightBlue)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, AppStyle.colors.lightBlue),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                CustomTextField(text = messageText, modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                        // send message
                        mMediaPlayer.start()
                        sendMessage(messagesRepo!!, messageText.value)
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
