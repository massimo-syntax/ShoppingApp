package com.example.shoppingapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.shoppingapp.data.model.Contact
import com.example.shoppingapp.data.model.Message
import com.example.shoppingapp.data.model.User
import com.example.shoppingapp.repository.MessagesRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class MessagesViewModel : ViewModel() {

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



    var contacts = mutableStateOf <List<Contact>> (emptyList())

    fun getContacts( conversationsMap:Map<String,String> ){

        val users : List<String> = conversationsMap.map { it.key }
        val allContacts = mutableListOf<Contact>()

        fun log(it:Any?){
            val tag:String = "messages"
            Log.wtf(tag,it.toString())
        }

        // the messaging feature is just an option of this app..
        // this function is goig to be called just once int the profile
        // so is going to be very cody instead to use existing functions that i have to search in internet
        // in every case the capability of firebase to call also firabse functions on complete is cool..

        // the fastest way possible, as much as i know about kotlin, is:
        // have a list with defined size of conversations id
        // ..and also of max value..
        val conversationId = mutableListOf<String>()
        val lastTimestampOfConversationId = mutableListOf<Long>()
        val lastMessageOfConversationId = mutableListOf<String>()

        conversationsMap.forEach { profileId, conversationid ->
            conversationId.add(conversationid)
            lastTimestampOfConversationId.add(0)
            lastMessageOfConversationId.add("")
        }

        log(conversationId.size)
        log(lastTimestampOfConversationId.size)
        log(lastTimestampOfConversationId)

        Firebase.firestore.collection("messages").get().addOnSuccessListener { messages ->

            val allMessages = messages.toObjects(Message::class.java)
            // then loop through the messages, because the list of conversation id is defined, and also smaller..
            allMessages.forEach { message ->
                log(message.conversation)
                log(conversationId)

                // [convId0][convId1][convId2]
                // [       ][       ][       ] --> timestamps list
                //              ^----- it was that, this message has the same conversation id!
                //                      lets check if the timestamp of this message is more..
                val index = conversationId.indexOf(message.conversation)
                if(index == -1) return@forEach
                // found the index in parallel lists where the conversation id is
                // yes we can index the whole message.. this i just a test, at this (my personal) timestamp (of now writing) the following user query to firebase is not even in this completelistener
                if(lastTimestampOfConversationId[index] < message.timestamp ){
                    // as said nice is also to have "lastmessagetoconversationid" <List<Messge>> [index]
                    // but iv already done the lists, after being hired i would first aks the senior or do with message
                    lastTimestampOfConversationId[index] = message.timestamp
                    lastMessageOfConversationId[index] = message.content
                }
            }
            // there is a nice way from firebase to ensure that the queries run sequentially...
            var counter = 0 // of course the value of the state<contact> is updated only at last query
            users.forEachIndexed { index, id ->
                Firebase.firestore
                    .collection("users")
                    .document(id)
                    .get().addOnSuccessListener{
                        val user = it.toObject(User::class.java)!!
                        // here we query the users, in a row..
                        // the map has key:userId value:Conversation id
                        // so we made a list of users, also a list of conversationId directly from the map so are the same index
                        // the other list of timestamp is already arranged parallel to this list
                        // so
                        // user list                        [same0][same1][same2][same3] ..
                        // conversation list                [same0][same1][same2][same3] ..
                        // last messsage timestamps         [same0][same1][same2][same3] ..
                        // last text                        [same0][same1][same2][same3] ..
                        // so is not even needed to search which index is..
                        // this index is the right one
                        val contact = Contact(
                            userName = user.name,
                            userId = user.id,
                            image = user.image,
                            conversationId = conversationsMap[id].toString(),
                            lastMessage= lastMessageOfConversationId[index],
                            lastMessageDatetime = lastTimestampOfConversationId[index],
                        )
                        allContacts.add(contact)
                        counter++
                        if(counter==users.size){
                            contacts.value = allContacts.toList().sortedBy { it.lastMessageDatetime }
                        }
                    }
            }






        }




    }

}
