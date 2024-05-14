package com.teocoding.tasktrackr.presentation.screen.photo

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.teocoding.tasktrackr.R
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun PhotoTackerScreen(
    onPhotoTaken: (Uri) -> Unit,
    contentPaddingValues: PaddingValues
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val controller = remember {
        LifecycleCameraController(context).apply {
            bindToLifecycle(lifecycleOwner)
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPaddingValues)
    ) {


        AndroidView(
            factory = { context ->

                PreviewView(context).apply {

                    this.controller = controller
                }
            },
            modifier = Modifier
                .fillMaxSize()
        )

        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primary,
            onClick = {
                takePicture(
                    context = context,
                    controller = controller,
                    onPhotoSaved = onPhotoTaken
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {

            Icon(
                painter = painterResource(id = R.drawable.ic_camera_24dp),
                contentDescription = null
            )
        }

    }

}


private fun takePicture(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoSaved: (Uri) -> Unit
) {

    val fileNameFormat = "yyyy-MM-dd-HH-mm-ss-SSS"

    val name = SimpleDateFormat(fileNameFormat, Locale.US)
        .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/TaskTrackr")
        }
    }


    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()


    controller.takePicture(outputOptions, ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(error: ImageCaptureException) {
                Log.e("PhotoTakerScreen", "Photo capture failed: ${error.message}", error)
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                outputFileResults.savedUri?.let {
                    onPhotoSaved(it)
                    Toast.makeText(
                        context,
                        context.getString(R.string.photo_capture_success), Toast.LENGTH_SHORT
                    ).show()

                } ?: run {
                    Toast.makeText(
                        context,
                        context.getString(R.string.photo_capture_failed), Toast.LENGTH_SHORT
                    ).show()
                }


            }
        }
    )
}