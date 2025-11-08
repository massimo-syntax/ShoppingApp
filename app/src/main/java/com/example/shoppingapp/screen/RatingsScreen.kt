package com.example.shoppingapp.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shoppingapp.components.BackButtonSimpleTopBar
import com.example.shoppingapp.components.CustomTextField
import com.example.shoppingapp.data.model.Product
import com.example.shoppingapp.repository.RemoteProductsRepository
import com.example.shoppingapp.viewmodel.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun RatingsScreen(profileViewModel: ProfileViewModel = viewModel()) {

    data class RatingState(
        val pid: String = "",
        var rating: Float = 0f,
        var comment: String = "",
        var todo: Boolean = false
    )


    val context = LocalContext.current
    fun tst(any: Any?) {
        Toast.makeText(context, any.toString(), Toast.LENGTH_LONG).show()
    }

    val profile by profileViewModel.profile

    var products by mutableStateOf<List<Product>>(emptyList())

    var ratings by mutableStateOf<List<RatingState>>(emptyList())

    var text by mutableStateOf("")

    var sliderPosition by mutableFloatStateOf(0f)


    LaunchedEffect(Unit) {
        // list of all products id
        profileViewModel.getProfile()

    }
    // just in case
    if (profile == null) return

    LaunchedEffect(Unit) {
        val completeList = mutableListOf<Product>()
        val _ratings = mutableListOf<RatingState>()

        profile!!.ratings.forEach {
            // in this particular case
            // digit only id is from Json
            if (it.isDigitsOnly()) {
                val jsonProduct = RemoteProductsRepository().getProduct(it)
                val p = Product(
                    title = jsonProduct.title,
                    images = jsonProduct.image
                )
                completeList.add(p)
                _ratings.add(RatingState(pid = p.id))
            } else { // from firebase
                Firebase.firestore.collection("products").document(it).get().addOnSuccessListener {
                    val p = it.toObject(Product::class.java)!!
                    completeList.add(p)
                    _ratings.add(RatingState(pid = p.id))
                }
            }
        }
        products = completeList.toList()
        ratings = _ratings.toList()
    }


    Scaffold(
        topBar = { BackButtonSimpleTopBar("Leave Rating") }
    ) { scaffoldPadding ->

        Column(
            Modifier
                .padding(scaffoldPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp)
        ) {

            products.forEach { product ->

                val rating = ratings.find { it.pid == product.id }!!
                val index = ratings.indexOf(rating)
                Column(
                    Modifier.fillMaxWidth()
                ) {
                    Row {
                        AsyncImage(
                            model = product.images.split(',').first(),
                            contentDescription = product.title,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.FillBounds
                        )
                        Column {
                            Text(product.title)
                            Text(rating.comment)
                            Text(rating.rating.toString())

                            if(rating.todo)
                            Slider(
                                value = sliderPosition,
                                onValueChange = {
                                    sliderPosition = it
                                    rating.rating = it
                                },
                                colors = SliderDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.secondary,
                                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                                ),
                                steps = 5,
                                valueRange = 0f..5f
                            )

                        }
                    }
                    Row {
                        if (rating.todo)
                            CustomTextField(
                                placeholderText = "write here",
                                modifier = Modifier.weight(1f),
                                valueChange = {
                                    text = it
                                    rating.comment = text
                                },
                                _text = text
                            )
                        else
                            Spacer(Modifier.weight(1f))

                        Checkbox(
                            checked = rating.todo,
                            onCheckedChange = { newValue ->
                                val updateList = ratings.toMutableList()
                                // checkbox is working like radiobutton
                                updateList.forEach { it.todo = false }
                                updateList[index] = updateList[index].copy(todo = newValue)
                                ratings = updateList.toList()
                                text = rating.comment
                            }
                        )

                    }
                }

            }


        }

    }


}