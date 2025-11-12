package com.example.shoppingapp.screen

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.Category
import com.example.shoppingapp.R
import com.example.shoppingapp.Routes
import com.example.shoppingapp.components.CustomTextField
import com.example.shoppingapp.components.UploadMultipleImages
import com.example.shoppingapp.viewmodel.ProductsViewModel

@Composable

fun NewProductUpload(viewModel: ProductsViewModel = viewModel()) {

    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Select a category") }
    // parameter to list to fill from UploadMultipleImages(pictures)
    val pictures = mutableListOf<String>()

    val categories = Category.entries.map { it.enam }
    val focusManager = LocalFocusManager.current

    // change status bar icon color hence background of header is dark, only in this composable
    val view = LocalView.current

    LaunchedEffect(Unit) {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
    }


    Scaffold(
        topBar = { TitleTopBar("Upload new product") },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {

            // upload multiple pictures screen
            UploadMultipleImages(pictures)

            Spacer(Modifier.height(16.dp))

            Text(
                "Title",
                style = MaterialTheme.typography.titleMedium,
                color = AppStyle.colors.darkBlue
            )

            CustomTextField(
                text = title,
                valueChange = {title = it},
                placeholderText = "Title",
                trailingIcon = {
                    IconButton(
                        modifier = Modifier
                            .height(36.dp)
                            .width(36.dp),
                        onClick = {
                            focusManager.clearFocus()
                        }) {
                        if (title.isEmpty())
                            Icon(
                                painterResource(R.drawable.icon_edit),
                                contentDescription = "Search",
                                tint = AppStyle.colors.lightBlue,
                                modifier = Modifier.height(36.dp)
                            )
                        else
                            Icon(
                                painterResource(R.drawable.icon_done),
                                contentDescription = "Exit search",
                                tint = AppStyle.colors.green,
                                modifier = Modifier.height(36.dp)
                            )
                    }
                },
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "Description",
                style = MaterialTheme.typography.titleMedium,
                color = AppStyle.colors.darkBlue
            )

            CustomTextField(
                text = description,
                placeholderText = "Description",
                valueChange = {description = it},
                singleLine = false,
                trailingIcon = {
                    IconButton(
                        modifier = Modifier
                            .height(36.dp)
                            .width(36.dp),
                        onClick = {
                            focusManager.clearFocus()
                        }) {
                        if (description.isEmpty())
                            Icon(
                                painterResource(R.drawable.icon_edit),
                                contentDescription = "Search",
                                tint = AppStyle.colors.lightBlue,
                                modifier = Modifier.height(36.dp)
                            )
                        else
                            Icon(
                                painterResource(R.drawable.icon_done),
                                contentDescription = "Exit search",
                                tint = AppStyle.colors.green,
                                modifier = Modifier.height(36.dp)
                            )
                    }
                },
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "Category",
                style = MaterialTheme.typography.titleMedium,
                color = AppStyle.colors.darkBlue
            )

            Text(
                    text = category,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppStyle.colors.darkBlue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

            //DropdownDemo(category)

            val firsHalf = categories.subList(0,categories.size/2)
            val secondHalf = categories.subList(categories.size/2, categories.size)

            // categories in list
            LazyRow {
                items(firsHalf ){
                    SuggestionChip(
                        modifier=Modifier.padding(horizontal = 6.dp),
                        label = { Text(it) },
                        onClick = {
                           category = it
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors().copy(
                            containerColor = AppStyle.colors.darkBlue,
                            labelColor = Color.White
                        ),
                    )
                }
            }

            // categories in second list
            LazyRow {
                items(secondHalf ){
                    SuggestionChip(
                        modifier=Modifier.padding(horizontal = 6.dp),
                        label = { Text(it) },
                        onClick = {
                           category = it
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors().copy(
                            containerColor = AppStyle.colors.darkBlue,
                            labelColor = Color.White
                        ),
                    )
                }
            }



            Text(
                "Price",
                style = MaterialTheme.typography.titleMedium,
                color = AppStyle.colors.darkBlue
            )
            CustomTextField(
                text = price,
                valueChange = {price= it},
                placeholderText = "Price",
                singleLine = true,
                keyboard = KeyboardType.Decimal,
                trailingIcon = {
                    IconButton(
                        modifier = Modifier
                            .height(36.dp)
                            .width(36.dp),
                        onClick = {
                            focusManager.clearFocus()
                        }) {
                        if (price.isEmpty())
                            Icon(
                                painterResource(R.drawable.icon_edit),
                                contentDescription = "Search",
                                tint = AppStyle.colors.lightBlue,
                                modifier = Modifier.height(36.dp)
                            )
                        else
                            Icon(
                                painterResource(R.drawable.icon_done),
                                contentDescription = "Exit search",
                                tint = AppStyle.colors.green,
                                modifier = Modifier.height(36.dp)
                            )
                    }
                },
            )


            Spacer(Modifier.height(32.dp))

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = AppStyle.colors.darkBlue),
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                onClick = {
                    if (title.isEmpty() || description.isEmpty() || price.isEmpty() || category.isEmpty() || pictures.isEmpty())
                        Toast.makeText(context, "All fields are required", Toast.LENGTH_LONG).show()
                    else {
                        var images = ""
                        pictures.forEach { images += "$it," }
                        // last char is a comma now
                        images = images.dropLast(1)

                        viewModel.uploadProduct(
                            title = title,
                            description = description,
                            price = price,
                            category = category,
                            images = images
                        ) {
                            Toast.makeText(
                                context,
                                "Product uploaded successfully",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_save),
                    contentDescription = "upload",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Upload product", style = TextStyle(fontSize = 18.sp), color = Color.White)
            }



            Spacer(Modifier.height(32.dp))


        }

    }


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTopBar(title: String, dark: Boolean = false) {
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = if (dark) AppStyle.colors.darkBlue else Color.White,
            scrolledContainerColor = if (dark) AppStyle.colors.darkBlue else Color.White,
            titleContentColor = if (!dark) AppStyle.colors.darkBlue else Color.White,
            subtitleContentColor = if (!dark) AppStyle.colors.darkBlue else Color.White,
            actionIconContentColor = if (!dark) AppStyle.colors.darkBlue else Color.White,
            navigationIconContentColor = if (!dark) AppStyle.colors.darkBlue else Color.White,
        ),
        title = { Text(title) },
        navigationIcon = {
            // NO RIPPLE ON BUTTON CLICK
            CompositionLocalProvider(LocalRippleConfiguration provides null) {
                IconButton(onClick = {
                    Routes.navController.popBackStack()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.icon_back),
                        contentDescription = "menu",
                        tint = if (dark) Color.White else AppStyle.colors.darkBlue,
                    )
                }
            }
        }
    )
}

