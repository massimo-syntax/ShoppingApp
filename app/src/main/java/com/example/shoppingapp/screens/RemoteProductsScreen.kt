package com.example.shoppingapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingapp.viewmodel.RemoteProductsViewModel

@Composable
fun RemoteProductsScreen(viewModel: RemoteProductsViewModel = viewModel()) {

    val products by viewModel.products

    LaunchedEffect(Unit) {
        viewModel.fetchProducts()
    }

    Column {
        if (products.isEmpty()) {
            // Show loading indicator or placeholder
            Text(text = "Loading...")
        } else {
            // Display the list of credit cards
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(products){product ->
                    Text(text = product.title)
                }
            }
        }
    }
}