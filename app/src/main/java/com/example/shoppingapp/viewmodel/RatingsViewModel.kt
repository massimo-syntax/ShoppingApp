package com.example.shoppingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.model.Rating
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class RatingsViewModel : ViewModel() {

    data class RatingUiState(
        val loading: Boolean = true,
        val result: List<Rating> = emptyList()
    )

    val ref = Firebase.firestore.collection("ratings")

    private val _ratings = MutableStateFlow<RatingUiState>(RatingUiState())
    val ratings: StateFlow<RatingUiState> = _ratings.asStateFlow()

    fun getRatings(productId: String) {
        _ratings.update{it.copy(loading = true)}
        ref
            .whereEqualTo("productId",productId)
            .get()
            .addOnSuccessListener {
                val list = it.toObjects(Rating::class.java)
                _ratings.update { it.copy(loading = false , result = list) }
            }
    }

    fun saveRatings(ratings: List<Rating>, callback: () -> Unit) {
        var count = 0
        viewModelScope.launch {
            val id = UUID.randomUUID().toString()
            ratings.forEach {
                it.id = id
                ref
                    .document(id)
                    .set(it)
                    .addOnSuccessListener {
                        count++
                        if (count == ratings.size) {
                            // also delete from my profile
                            val userRef = Firebase.firestore
                                .collection("users")
                                .document(Firebase.auth.uid!!)
                            val updates = hashMapOf<String, Any>(
                                "ratings" to FieldValue.delete(),
                            )
                            userRef
                                .update(updates)
                                .addOnCompleteListener {
                                    callback()
                                }
                        }
                    }
            }
        }

    }


}