package com.example.shoppingapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shoppingapp.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class AuthViewModel : ViewModel() {

    private val auth = Firebase.auth
    private val DB = Firebase.firestore

    fun signup(email:String , name: String , password:String, resultCallback:(successful:Boolean , message:String?)->Unit ){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    // store name - email in firestore as soon as auth completes
                    // nothing is null
                    val userId = it.result.user?.uid!!
                    val auth_email = it.result.user?.email!!
                    val newUser = User(userId,auth_email, name)
                    DB
                        .collection("users")
                        .document(userId)
                        .set(newUser)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                // feedback to UI
                                resultCallback(true,null)
                            }else{
                                // feedback to UI
                                resultCallback(false, "Databse ERROR: user could not be saved")
                            }
                        }
                }else{
                    // feedback to UI
                    resultCallback(false,it.exception?.localizedMessage)
                }
            }
            .addOnFailureListener { exception ->
                resultCallback(false, exception.message)
            }
    }


}