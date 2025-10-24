package com.example.shoppingapp.screen.mainScreenPages

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shoppingapp.data.model.UiProductWithFieldsFromRoom
import com.example.shoppingapp.data.remote.RemoteProduct
import com.example.shoppingapp.repository.RemoteProductsRepository
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.viewmodel.ProductsViewModel


@Composable
fun Cart(modifier: Modifier = Modifier ,  viewModel: ProductsViewModel = viewModel()){

    val roomRepo = SelectedProductsRepository(LocalContext.current)
    val remoteRepo = RemoteProductsRepository()

    var jsonApiCart by remember { mutableStateOf<List<UiProductWithFieldsFromRoom>>(emptyList()) }

    val firebaseProductsUiState by viewModel.uiProducts.collectAsState()

    val ctx = LocalContext.current
    fun toast( message:Any ){
        Toast.makeText(ctx,message.toString(),Toast.LENGTH_SHORT).show()

    }

    LaunchedEffect(Unit) {

        val cartIds = roomRepo.getCart().map { it.productId }
        val _jsonApiIds = mutableListOf<String>()
        val _firebaseIds = mutableListOf<String>()
        val _jsonApiCart = mutableListOf<UiProductWithFieldsFromRoom>()

        cartIds.forEach {
            if(it.length > 1){ // product is stored in fierbase
                _firebaseIds.add(it)
            } else { // product is from json api
                _jsonApiIds.add(it)
                val jsonProductRemote:RemoteProduct = remoteRepo.getProduct(it)
                val jsonProduct = UiProductWithFieldsFromRoom(
                    id = jsonProductRemote.id,
                    title = jsonProductRemote.title,
                    description = jsonProductRemote.description,
                    images = jsonProductRemote.image,
                    price = jsonProductRemote.price,
                    category = jsonProductRemote.category,
                    rating = "0.0"
                )
                _jsonApiCart.add(jsonProduct)
            }
        }
        jsonApiCart = _jsonApiCart.toList()
        viewModel.getDefinedListOfProducts(_firebaseIds.toList())

    }


    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ){

        Spacer(Modifier.height(20.dp))

        if(firebaseProductsUiState.fetching) CircularProgressIndicator()
        if(firebaseProductsUiState.result.isNotEmpty()){

            val products = firebaseProductsUiState.result

            // print products from firebase
            products.forEach {
                CartItem(it)
            }

        }

        // print products from json api
        if(jsonApiCart.isNotEmpty()){
            jsonApiCart.forEach {
                CartItem(it)
            }
        }

    }



}

@Composable
fun CartItem(product: UiProductWithFieldsFromRoom , quantity:Int = 0){

    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        AsyncImage(
            model = product.images.split(',').first(),
            contentDescription = product.title,
            modifier = Modifier.size(100.dp)
        )

        Spacer(Modifier.width(10.dp))

        Text(product.title)

    }

}