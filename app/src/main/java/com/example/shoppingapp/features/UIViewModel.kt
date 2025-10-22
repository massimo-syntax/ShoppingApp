package com.example.shoppingapp.features

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.uploadcare.android.library.api.UploadcareClient
import com.uploadcare.android.library.api.UploadcareFile
import com.uploadcare.android.library.callbacks.UploadFileCallback
import com.uploadcare.android.library.callbacks.UploadFilesCallback
import com.uploadcare.android.library.exceptions.UploadcareApiException
import com.uploadcare.android.library.upload.FileUploader
import com.uploadcare.android.library.upload.MultipleFilesUploader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.absoluteValue


class UIViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    private val _uploadProgress = MutableStateFlow(0.0.toFloat())
    val uploadProgress = _uploadProgress.asStateFlow()

    private val client = UploadcareClient("16f1bb7a0b596194a8c2", "9fb54755b81cc97b3e45")

    fun onEvent(event: UIEvent) {
        when (event) {
            is UIEvent.SingleImageChanged -> {
                uploadSingleImage(event.context, event.uri)
            }
            is UIEvent.MultipleImageChanged -> {
                uploadMultipleImages(event.context, event.uris)
            }
        }
    }

    private fun uploadSingleImage(context: Context, uri: Uri) {
        _uiState.update { it.copy(isUploading = true) }
        // here in single image the image has to just one..
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
                ) {
                    _uploadProgress.value = progress.toFloat()
                }

                override fun onSuccess(result: UploadcareFile) {
                    val imageResult =
                        ImageResults(
                            uid = result.uuid,
                            imageUrl = result.originalFileUrl.toString()
                        )
                    images.add(imageResult)
                    _uploadProgress.value = 0.0.toFloat()
                    _uiState.update { it.copy(isUploading = false, images = images) }
                }
            }
        )
    }



    private fun uploadMultipleImages(context: Context, uris: List<Uri>) {
        _uiState.value = UIState(isUploading = true)
        val images = _uiState.value.images
        val uploader = MultipleFilesUploader(client, uris, context).store(true)

        uploader.uploadAsync(
            object : UploadFilesCallback {
                override fun onFailure(e: UploadcareApiException) {
                    Log.i("ERROR", e.message.toString())
                }

                override fun onProgressUpdate(
                    bytesWritten: Long,
                    contentLength: Long,
                    progress: Double
                ) {
                    _uploadProgress.value = progress.toFloat()
                }

                override fun onSuccess(result: List<UploadcareFile>) {
                    result.forEach {
                        images.add(
                            ImageResults(uid = it.uuid, imageUrl = it.originalFileUrl.toString())
                        )
                    }
                    _uiState.update { it.copy(isUploading = false, images = images) }
                }
            }
        )
    }



}

