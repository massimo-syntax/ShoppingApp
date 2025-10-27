package com.example.shoppingapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.shoppingapp.data.model.Message
import com.example.shoppingapp.repository.MessagesRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class MessagingViewModel : ViewModel() {

    var messages by mutableStateOf<List<Message>>(emptyList())
        private set


    fun getAndListenMessages(messageRepo: MessagesRepository){
        messageRepo.getMessages { newMessages ->
            messages = newMessages
        }
    }

    fun sendMessage(messageRepo: MessagesRepository , text: String) {
        messageRepo.sendMessage(text)
    }
}
