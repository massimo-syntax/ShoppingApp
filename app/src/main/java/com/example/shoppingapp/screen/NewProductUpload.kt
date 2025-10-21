package com.example.shoppingapp.screen

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.Routes
import com.example.shoppingapp.components.BackButtonSimpleTopBar
import com.example.shoppingapp.components.CustomTextField
import com.example.shoppingapp.viewmodel.ProductsViewModel

@Composable

fun NewProductUpload(viewModel: ProductsViewModel = viewModel() ){


    var title = remember { mutableStateOf("") }
    var description = remember { mutableStateOf("") }



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


            Text("Title")
            CustomTextField(
                text = title,
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.height(36.dp).width(36.dp),
                        onClick = {
                            focusManager.clearFocus()
                        }) {
                        if(title.value.isEmpty())
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
                        if(description.value.isEmpty())
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

            // Button for picking a photo, handling upload state, and launching image picker
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

            TextButton(
                onClick = {
                    // go to multiple pictures upload page
                    Routes.navController.navigate(Routes.UploadMultiplePictures)

                },
            ) {
                Text("go to pictures upload page ->")
            }


        }

    }


}
