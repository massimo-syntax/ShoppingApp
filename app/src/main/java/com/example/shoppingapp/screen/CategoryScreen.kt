package com.example.shoppingapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingapp.Category
import com.example.shoppingapp.components.BackButtonSimpleTopBar
import com.example.shoppingapp.components.MainProductCard
import com.example.shoppingapp.components.SimpleStandardTopBar
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.viewmodel.ProductsViewModel
import kotlinx.coroutines.runBlocking


@Composable
fun CategoryScreen(category: Category, viewModel: ProductsViewModel = viewModel () ){


    val uiState by viewModel.uiProducts.collectAsState()

    val roomRepo = SelectedProductsRepository(LocalContext.current)

    LaunchedEffect(Unit) {
        viewModel.getCategory(category, roomRepo)
    }


    Scaffold (
        topBar = { BackButtonSimpleTopBar(category.enam) }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
        ) {

            Text(category.description)

            Spacer(Modifier.height(20.dp))

            if(uiState.fetching) CircularProgressIndicator()
            if(uiState.result.isNotEmpty()){
                LazyVerticalGrid(GridCells.Fixed(2)) {
                    itemsIndexed(uiState.result){ index , product ->
                        MainProductCard(
                            product = product,
                            isInFav = product.fav,
                            onToggleFav = {
                                runBlocking {
                                    roomRepo.toggleFav(product.id)
                                }
                                product.fav = !product.fav
                                viewModel.updateList(index,product)
                            }
                        )
                    }
                }
            }


        }

    }

}