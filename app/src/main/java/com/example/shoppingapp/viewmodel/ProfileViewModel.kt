package com.example.shoppingapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.shoppingapp.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class ProfileViewModel : ViewModel() {

    private val userId = Firebase.auth.currentUser!!.uid
    private val DB = Firebase.firestore
    private val _profile = mutableStateOf<User?>(null)
    val profile = _profile


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
                val user = document.toObject(User::class.java)
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