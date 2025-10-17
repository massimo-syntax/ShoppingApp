package com.example.shoppingapp.features

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.uploadcare.android.library.api.UploadcareClient
import com.uploadcare.android.library.api.UploadcareFile
import com.uploadcare.android.library.callbacks.UploadFileCallback
import com.uploadcare.android.library.exceptions.UploadcareApiException
import com.uploadcare.android.library.upload.FileUploader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class UIViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())

    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    private val client = UploadcareClient("16f1bb7a0b596194a8c2", "9fb54755b81cc97b3e45")

    fun onEvent(event: UIEvent) {
        when (event) {
            is UIEvent.SingleImageChanged -> {
                uploadSingleImage(event.context, event.uri)
            }
        }
    }

    private fun uploadSingleImage(context: Context, uri: Uri) {
        _uiState.update { it.copy(isUploading = true) }
        val images = _uiState.value.images

        val uploader = FileUploader(client, uri, context).store(true)
        uploader.uploadAsync(

            object : UploadFileCallback {
                override fun onFailure(e: UploadcareApiException) {

                    Log.wtf("ERROR_UPLOAD", e.message.toString())
                    Log.wtf("ERROR_UPLOAD", e.message.toString())
                    Log.wtf("ERROR_UPLOAD", e.message.toString())
                    Log.wtf("ERROR_UPLOAD", e.message.toString())
                    Log.wtf("ERROR_UPLOAD", e.message.toString())
                    Log.wtf("ERROR_UPLOAD", e.message.toString())
                    Log.wtf("ERROR_UPLOAD", e.message.toString())
                    Log.wtf("ERROR_UPLOAD", e.message.toString())
                    Log.wtf("ERROR_UPLOAD", e.message.toString())
                    Log.wtf("ERROR_UPLOAD", e.message.toString())


                }

                override fun onProgressUpdate(
                    bytesWritten: Long,
                    contentLength: Long,
                    progress: Double
                ) {}

                override fun onSuccess(result: UploadcareFile) {
                    val imageResult =
                        ImageResults(
                            uid = result.uuid,
                            imageUrl = result.originalFileUrl.toString()
                        )
                    images.add(imageResult)
                    _uiState.update { it.copy(isUploading = false, images = images) }
                }
            }
        )
    }
}

