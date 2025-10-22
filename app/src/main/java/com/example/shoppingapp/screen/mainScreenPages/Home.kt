package com.example.shoppingapp.screen.mainScreenPages

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingapp.components.EndlessPager
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.viewmodel.ProductsViewModel


@Composable
fun Home(modifier: Modifier = Modifier ,  productsViewModel: ProductsViewModel = viewModel()) {



    val context = LocalContext.current
    fun toast(message:Any){
        Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show()
    }

    // get cart and fav from ROOM
    val roomRepo = SelectedProductsRepository(context)

    // products from JSON api
    var uiProducts  = productsViewModel.uiProducts.collectAsState()



    LaunchedEffect(Unit) {
        productsViewModel.getAllProducts(roomRepo)
    }


    Column(modifier) {

        EndlessPager()

        Text(uiProducts.value.toString())

    }

}









