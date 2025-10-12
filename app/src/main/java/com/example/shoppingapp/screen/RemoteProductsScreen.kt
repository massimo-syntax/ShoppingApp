package com.example.shoppingapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingapp.R
import com.example.shoppingapp.viewmodel.RemoteProductsViewModel

@Composable
fun RemoteProductsScreen(modifier: Modifier = Modifier, viewModel: RemoteProductsViewModel = viewModel()) {

    val products by viewModel.products

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchProducts()
    }

    Column (
        modifier.padding(horizontal = 8.dp),
    ) {

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {searchQuery = it},
            label = {null},
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.adaptivecarticon_foreground),
                    contentDescription = "hello",
                    Modifier.height(32.dp)
                )
            }
        )



        Spacer(Modifier.height(10.dp))

        if (products.isEmpty()) {
            // Show loading indicator or placeholder
            Text(text = "Loading...")
        } else {
            // Display the list of credit cards
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                val filtered  = products.filter { it.title.lowercase().contains(searchQuery.lowercase()) }
                items(filtered){ product ->
                    Text(text = product.title)
                }
            }
        }
    }
}