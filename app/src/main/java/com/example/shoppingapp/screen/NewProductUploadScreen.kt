package com.example.shoppingapp.screen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.components.BackButtonSimpleTopBar
import com.example.shoppingapp.components.CustomTextField
import com.example.shoppingapp.components.DropdownDemo
import com.example.shoppingapp.components.UploadMultipleImages
import com.example.shoppingapp.viewmodel.ProductsViewModel

@Composable

fun NewProductUpload(viewModel: ProductsViewModel = viewModel() ){

    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    val category = remember { mutableStateOf("") }
    // parameter to list to fill from UploadMultipleImages(pictures)
    val pictures = mutableListOf<String>()


    Scaffold (
        topBar = {BackButtonSimpleTopBar("Upload new product", true)},
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {

            val focusManager = LocalFocusManager.current


            Text("Pictures")

            // upload multiple pictures screen
            UploadMultipleImages(pictures)


            Text("Title")
            CustomTextField(
                text = title,
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.height(36.dp).width(36.dp),
                        onClick = {
                            focusManager.clearFocus()
                        }) {
                        if(title.isEmpty())
                            Icon(
                                painterResource(R.drawable.icon_edit),
                                contentDescription = "Search",
                                tint = AppStyle.colors.darkBlule,
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

            Text("Description")

            CustomTextField(
                text = description,
                singleLine = false,
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.height(36.dp).width(36.dp),
                        onClick = {
                            focusManager.clearFocus()
                        }) {
                        if(description.isEmpty())
                            Icon(
                                painterResource(R.drawable.icon_edit),
                                contentDescription = "Search",
                                tint = AppStyle.colors.darkBlule,
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

            Text("Category")
            DropdownDemo(category)

            Text("Price")
            CustomTextField(
                text = price,
                singleLine = true,
                keyboard = KeyboardType.Decimal,
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.height(36.dp).width(36.dp),
                        onClick = {
                            focusManager.clearFocus()
                        }) {
                        if(price.isEmpty())
                            Icon(
                                painterResource(R.drawable.icon_edit),
                                contentDescription = "Search",
                                tint = AppStyle.colors.darkBlule,
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
            // Save product
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppStyle.colors.darkBlule,
                    contentColor = Color.White,
                ),
                // start image picker when not uploading
                onClick = {

                    if(title.isEmpty()||description.isEmpty()||price.isEmpty()||category.value.isEmpty()||pictures.isEmpty())
                        Toast.makeText(context, "All fields are required", Toast.LENGTH_LONG).show()
                    else{
                        var images = ""
                        pictures.forEach { images += "$it,"}
                        // last char is a comma now
                        images = images.dropLast(1)

                        viewModel.uploadProduct(
                            title = title,
                            description = description,
                            price = price,
                            category = category.value,
                            images = images
                        ){
                            Toast.makeText(context, "Product uploaded successfully", Toast.LENGTH_LONG).show()
                        }
                    }

                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_save),
                    contentDescription = "save picture",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(text = "Upload product", style = TextStyle(fontSize = 18.sp))
            }

            Spacer(Modifier.height(20.dp))


        }

    }


}
