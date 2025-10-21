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

class ProfileVIewModel : ViewModel() {

    private val userId = Firebase.auth.currentUser!!.uid
    private val DB = Firebase.firestore

    private val _profile = mutableStateOf<User?>(null)
    val profile = _profile


    // cannot deserialize user with .toObject() - as User - get(User::java.class) or found in internet
    private fun documentToUser(map:Map<String,Any>?):User?{
        return if (map!=null)User(
            id = map["id"].toString(),
            name = map["name"].toString(),
            email = "",
            image = (map["image"] ?: "") as String
        )else null
    }


    fun getProfile(profileId:String = userId){
        DB  .collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener {
                val user = documentToUser(it.data)
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

}