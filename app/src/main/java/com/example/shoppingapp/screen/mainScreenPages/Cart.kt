package com.example.shoppingapp.screen.mainScreenPages

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shoppingapp.R
import com.example.shoppingapp.data.model.UiProductWithFieldsFromRoom
import com.example.shoppingapp.data.model.UserSelectedProduct
import com.example.shoppingapp.data.remote.RemoteProduct
import com.example.shoppingapp.repository.RemoteProductsRepository
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.viewmodel.ProductsViewModel
import com.example.shoppingapp.viewmodel.UserSelectedViewModel


@Composable
fun Cart(modifier: Modifier = Modifier , /* viewModel: ProductsViewModel = viewModel() */ viewModel: UserSelectedViewModel = viewModel() ){

    val uiState = viewModel.uiStateCart.collectAsState()
    val roomRepo = SelectedProductsRepository(LocalContext.current)

    LaunchedEffect(Unit) {
        if(uiState.value.list.isEmpty()){

            viewModel.getAllCart(roomRepo)

        }
        viewModel.getAllCart(roomRepo)

    }


    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ){
        Spacer(Modifier.height(20.dp))

        if(uiState.value.list.isEmpty()){
            Column(
                Modifier.fillMaxSize().background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("no items in the cart")
            }
        }

        if(uiState.value.roomDataLoaded && uiState.value.firebaseDataLoaded){
            uiState.value.list.forEach {
                CartItem(it)
            }
        }else{
            CircularProgressIndicator()
        }

    }



}

@Composable
fun CartItem(product: UserSelectedProduct , quantity:Int = 0){

    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        AsyncImage(
            model = product.mainPicture,
            contentDescription = product.title,
            modifier = Modifier.size(100.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(product.title)
    }

}