package com.example.shoppingapp.repository

import android.util.Log
import androidx.compose.runtime.key
import com.example.shoppingapp.data.model.Message
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.firestore
import java.util.UUID
import kotlin.collections.emptyList

class MessagesRepository( conversationId:String ) {
    private val DB = Firebase.firestore

    private val messagesCollection = DB.collection("messages")
    private val conversation = conversationId
    private val myId = Firebase.auth.uid

    fun sendMessage(text:String) {
        val message = Message(
            senderId = myId!!,
            content = text,
            conversation = conversation,
        )
        messagesCollection.add(message)
    }

    val TAG = "snapshot"
    fun getMessages(callback: (List<Message>) -> Unit) {
        val hello = conversation
        messagesCollection
            .whereEqualTo("conversation", hello)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "listen:error", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> Log.d(TAG, "New city: ${dc.document.data}")
                        DocumentChange.Type.MODIFIED -> Log.d(TAG, "Modified city: ${dc.document.data}")
                        DocumentChange.Type.REMOVED -> Log.d(TAG, "Removed city: ${dc.document.data}")
                    }
                }
            }
        Log.w(TAG, "conversation = $hello")

        //callback(messages)
    }
}
