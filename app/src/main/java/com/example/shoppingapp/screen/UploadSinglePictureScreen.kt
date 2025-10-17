package com.example.shoppingapp.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.shoppingapp.R
import com.example.shoppingapp.features.UIEvent
import com.example.shoppingapp.features.UIViewModel


@Composable
fun UploadSinglePictureScreen(){

    val ctx = LocalContext.current
    fun toast(message:Any){
        Toast.makeText(ctx,message.toString(),Toast.LENGTH_SHORT).show()
    }

    PhotoScreen()


}

@Composable
fun PhotoScreen(viewModel: UIViewModel = viewModel()) {
    // Collects and observes the UI state using the ViewModel
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current // Retrieves the current context

    // Remembers the launcher for picking a single image from media and handles its result
    val singleImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { // Handles the selected image URI
                UIEvent.SingleImageChanged(it, context) // Fires event for single image upload
            }?.let { viewModel.onEvent(it) } // Passes the event to the ViewModel
        }
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            // Displays a title for the file uploader
            Text(
                text = "UC file uploader in Android",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.size(20.dp)) // Adds spacing between elements

            // Button for picking a photo, handling upload state, and launching image picker
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0073E6),
                    contentColor = Color.White,
                ),
                onClick = {
                    if (!uiState.isUploading) {
                        singleImagePickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                }
            ) {
                // Displays appropriate content based on upload state
                if (!uiState.isUploading) {
                    Row {
                        Icon(painter = painterResource(R.drawable.icon_sell), contentDescription = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "upload a file", style = TextStyle(fontSize = 18.sp))
                    }
                } else {
                    CircularProgressIndicator(
                        color = Color.White,
                    )
                }
            }

            // Displays the uploaded images in a grid if available, or a message if none uploaded
            if (uiState.images.isNotEmpty()) {
                Toast.makeText(context, uiState.images[0].imageUrl, Toast.LENGTH_SHORT).show()

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                ) {
                    items(uiState.images.size) { index ->
                        NetworkImage(imageUrl = uiState.images[index].imageUrl)
                        Toast.makeText(context, uiState.images[index].imageUrl, Toast.LENGTH_SHORT).show()
                    }
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
        }
    }
}

// Composable function for displaying a network image fetched by URL
@Composable
fun NetworkImage(imageUrl: String) {
    val imageModifier = Modifier
        .padding(8.dp)
        .size(150.dp)
    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
        ),
        contentDescription = "Network Image",
        contentScale = ContentScale.Crop,
        modifier = imageModifier
    )
}

