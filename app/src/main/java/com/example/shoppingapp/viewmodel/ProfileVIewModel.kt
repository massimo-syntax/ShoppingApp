package com.example.shoppingapp.viewmodel

import android.provider.ContactsContract
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.shoppingapp.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.util.UUID

class ProfileVIewModel : ViewModel() {

    private val userId = Firebase.auth.currentUser!!.uid
    private val DB = Firebase.firestore

    private val _profile = mutableStateOf<User?>(null)
    val profile = _profile


    // cannot deserialize user with .toObject() - as User - get(User::java.class) or found in internet
    private fun documentToUser(map: Map<String, Any>?): User? {
        return if (map != null) User(
            id = map["id"].toString(),
            name = map["name"].toString(),
            email = "",
            image = (map["image"] ?: "") as String,
            chat = (map["chat"] ?: mutableMapOf<String,String>()) as MutableMap<String, String>
        ) else null
    }


    fun getProfile(profileId:String = userId){
        DB  .collection("users")
            .document(profileId)
            .get()
            .addOnSuccessListener { document ->
                if (document.data == null){
                    val user = User(
                        id = "",
                        email = "",
                        name = "Deleted User",
                        image = "",
                        chat = mutableMapOf()
                    )
                    _profile.value = user
                    return@addOnSuccessListener
                }

                // val user = document.toObject(User::class.java)
                // java.lang.RuntimeException: Could not deserialize object. Class com.example.shoppingapp.data.model.User does not define a no-argument constructor. If you are using ProGuard, make sure these constructors are not stripped

                val user = documentToUser(document.data)
                _profile.value = user
            }
    }

    fun updateProfile(url:String , onComplete : ( Success:Boolean , message:String ) -> Unit ){
        DB  .collection("users")
            .document(userId)
            .update("image" , url )
            .addOnSuccessListener {
                onComplete(true , "Picture saved")
            }
            .addOnFailureListener {
                onComplete(false , "Database error: ${it.message.toString()}")
            }
    }

    private fun connectUsers( foreignId:String ) :String {
        val myId = userId
        val newChatId = UUID.randomUUID().toString()

        DB.collection("users")
            .document(myId)
            .update(
                mapOf("chat.$foreignId" to newChatId)
            )

        DB.collection("users")
            .document(foreignId)
            .update(
                mapOf("chat.$myId" to newChatId)
            )

        return newChatId
    }

    fun requestConversation( idReceiver:String ) : String {

        // no conversations in map chat
        if(_profile.value?.chat.isNullOrEmpty()) {
            // write in firebase for 2th users in user.chat
            val connectionId = connectUsers(idReceiver)
            // also in my profile just to have it
            _profile.value?.chat[idReceiver] = connectionId
            return connectionId
        }

        // id receiver present in map, just return conversation id otherwise same as above
        if( _profile.value?.chat!!.containsKey(idReceiver) )
            return _profile.value?.chat!![idReceiver]!!
        else{
            val connectionId = connectUsers(idReceiver)
            _profile.value?.chat[idReceiver] = connectionId
            return connectionId
        }

    }

}