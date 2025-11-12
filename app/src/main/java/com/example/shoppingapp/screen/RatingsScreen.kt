package com.example.shoppingapp.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.Routes
import com.example.shoppingapp.components.BackButtonSimpleTopBar
import com.example.shoppingapp.components.CustomTextField
import com.example.shoppingapp.components.RatingBar
import com.example.shoppingapp.data.model.Product
import com.example.shoppingapp.data.model.Rating
import com.example.shoppingapp.repository.RemoteProductsRepository
import com.example.shoppingapp.viewmodel.ProfileViewModel
import com.example.shoppingapp.viewmodel.RatingsViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import kotlin.math.round

@Composable
fun RatingsScreen(profileViewModel: ProfileViewModel = viewModel() , ratingsViewModel: RatingsViewModel = viewModel() ) {

    data class RatingState(
        val pid: String = "",
        var rating: Float = 0f,
        var comment: String = "",
        var todo: Boolean = false,
        var checked: Boolean = false
    )

    val context = LocalContext.current
    fun tst(any: Any?) {
        Toast.makeText(context, any.toString(), Toast.LENGTH_LONG).show()
    }

    val profile by profileViewModel.profile

    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var ratings by remember { mutableStateOf<List<RatingState>>(emptyList()) }
    var text by remember { mutableStateOf("") }
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // list of all products id
        profileViewModel.getProfile()
    }
    // very important
    if (profile == null) return

    LaunchedEffect(Unit) {
        val completeList = mutableListOf<Product>()
        val _ratings = mutableListOf<RatingState>()
        var count = 0
        profile!!.ratings.forEach {
            val productId = it.key
            // in this particular case
            // digit only id is from Json
            if (productId.isDigitsOnly()) {
                val jsonProduct = RemoteProductsRepository().getProduct(productId)
                val p = Product(
                    id = jsonProduct.id,
                    title = jsonProduct.title,
                    images = jsonProduct.image
                )
                completeList.add(p)
                _ratings.add(RatingState(pid = p.id))
                count ++
            } else { // from firebase
                Firebase.firestore.collection("products").document(productId).get().addOnSuccessListener {
                    val p = it.toObject(Product::class.java)!!
                    completeList.add(p)
                    _ratings.add(RatingState(pid = p.id))
                    count ++
                }
            }
        }

        var allThere = false
        while(!allThere){
            delay(100)
            if(count == profile!!.ratings.size){
                products = completeList.toList()
                ratings = _ratings.toList()
                allThere = true
            }
        }



    }


    Scaffold(
        topBar = { BackButtonSimpleTopBar("Leave Rating") }
    ) { scaffoldPadding ->

        Column(
            Modifier
                .padding(scaffoldPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp)
                .imePadding()
        ) {

            // skip all ratings -text button-
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = {
                        ratingsViewModel.deleteAllProductsForRating {
                            tst("You don't have to review the products")
                        }
                        Routes.navController.popBackStack()
                    },
                ) {
                    Text("Skip all ratings")
                    Spacer(Modifier.width(10.dp))
                    Icon(
                        painter = painterResource(R.drawable.icon_further),
                        contentDescription = "skip, further",
                        tint = AppStyle.colors.darkBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // all products for rating
            products.forEach { product ->
                val rating = ratings.find { it.pid == product.id }!!
                val index = ratings.indexOf(rating)
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable(
                            onClick = {
                                val updateList = ratings.toMutableList()
                                // when pressing on a product,
                                // the focus is on this

                                // exit focus for all other products
                                updateList.forEach { it.todo = false }
                                // set focus on this product
                                updateList[index] = updateList[index].copy(todo = true)
                                // trigger recomposition
                                ratings = updateList.toList()
                                // prepare written text to have already in textfield
                                text = rating.comment
                                sliderPosition = rating.rating
                            }
                        )
                ) {
                    Row {
                        AsyncImage(
                            model = product.images.split(',').first(),
                            contentDescription = product.title,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.FillBounds
                        )
                        // title, Comment, rating bar
                        Column {
                            // title
                            Text(
                                product.title,
                                style = TextStyle(
                                    color = AppStyle.colors.darkBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                            // comment
                            Text(
                                rating.comment,
                                style = TextStyle(
                                    color = AppStyle.colors.darkBlue,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )

                            Text(rating.rating.toString())

                            RatingBar(
                                rating = rating.rating,
                                iconSize = 28
                            )
                        }
                    }
                    Row {
                        if (rating.todo)
                            Slider(
                                modifier = Modifier.height(26.dp),
                                value = sliderPosition,
                                onValueChange = {
                                    sliderPosition = round(it)
                                    rating.rating = round(it)
                                },
                                colors = SliderDefaults.colors(
                                    thumbColor = AppStyle.colors.gold,
                                    activeTrackColor = AppStyle.colors.lightBlue,
                                    inactiveTrackColor = AppStyle.colors.darkBlue,
                                ),
                                steps = 3,
                                valueRange = 1f..5f
                            )
                    }
                    Spacer(Modifier.height(16.dp))
                    Row {
                        if (rating.todo) {
                            CustomTextField(
                                placeholderText = "write here",
                                modifier = Modifier.weight(1f),
                                valueChange = {
                                    text = it
                                    rating.comment = text
                                },
                                text = text
                            )

                        } else Spacer(Modifier.weight(1f))

                        Checkbox(
                            checked = rating.checked,
                            onCheckedChange = { newValue ->
                                //rating.checked = newValue
                                // update list for recomposition
                                val updateList = ratings.toMutableList()
                                updateList[index] = updateList[index].copy(checked = newValue)
                                ratings = updateList.toList()
                            }
                        )
                    }

                }

            }
            // explanation text, button send
            Column(
                modifier = Modifier.padding(10.dp)
            ) {

                Text(
                    "Products must have a comment, or have a rating more that 1 star to be saved",
                    style = TextStyle(
                        color = AppStyle.colors.darkBlue,
                        fontWeight = FontWeight.Medium
                    ),
                )

                Spacer(Modifier.height(20.dp))

                // send rating
                Button(
                    onClick = {
                        loading = true
                        // filter 0 without commnet or not checked
                        val ratingsFiltered = ratings.filter { it.checked && (it.rating.toInt() > 1 || it.comment.isNotEmpty()) }
                        // create the the right object to save in database
                        val ratingsToSave = ratingsFiltered.map{
                            Rating(
                                productId = it.pid,
                                userId = Firebase.auth.uid,
                                rating = it.rating,
                                comment = it.comment
                            )
                        }

                        tst(ratingsToSave)
                        ratingsViewModel.saveRatings(ratingsToSave){
                            tst("Ratings are saved")
                            loading = false
                            Routes.navController.popBackStack()
                        }

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppStyle.colors.darkBlue),
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_star),
                        contentDescription = "rating icon on button",
                        tint = AppStyle.colors.gold,
                        modifier = Modifier.size(36.dp)
                    )
                    if (!loading)
                        Text(
                            text = "Save ratings",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    else
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                            color = Color.White
                        )

                }
            }
            // spacer on bottom
            Spacer(Modifier.height(36.dp))

        }

    }

}

