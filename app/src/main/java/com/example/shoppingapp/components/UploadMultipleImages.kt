package com.example.shoppingapp.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.features.UIEvent
import com.example.shoppingapp.features.UIViewModel


@Composable
fun UploadMultipleImages(images: MutableList<String>, viewModel: UIViewModel = viewModel()) {

    val uiState by viewModel.uiState.collectAsState()
    val progress by viewModel.uploadProgress.collectAsState()
    val context = LocalContext.current // Retrieves the current context


    val multipleImagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(),
            onResult = { uris -> viewModel.onEvent(UIEvent.MultipleImageChanged(uris, context)) }
        )


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (uiState.images.isNotEmpty()) {
            LazyRow(
                contentPadding = PaddingValues(6.dp),
                //modifier = Modifier.height(200.dp)
            ) {
                items(uiState.images.size) { index ->
//                    NetworkImage(imageUrl = uiState.images[index].imageUrl)
                    AsyncImage(
                        model = uiState.images[index].imageUrl,
                        contentDescription = "product image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .padding(4.dp)
                    )
                }
            }

            // just add the images, so the user can also forget some so he can upload again to have all the images together..
            images.removeAll(images)
            uiState.images.forEach {
                images.add(it.imageUrl)
            }

        } else {
            Spacer(modifier = Modifier.height(40.dp))
            if (!uiState.isUploading) {
                Text(
                    text = "No uploaded Images yet",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }


        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (!uiState.isUploading) {
                    multipleImagePickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = AppStyle.colors.darkBlule),
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(6.dp)
        ) {
            if (!uiState.isUploading) {
                Icon(
                    painter = painterResource(R.drawable.icon_plus),
                    contentDescription = "rating icon on button",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Pick multiple photos", style = TextStyle(fontSize = 18.sp))

            } else {
                CircularProgressIndicator(
                    progress = { progress },
                    color = Color.White,
                    strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth,
                    trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                    strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                )
            }

        }


    }


}