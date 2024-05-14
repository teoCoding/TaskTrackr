package com.teocoding.tasktrackr.presentation.screen.photo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class CameraPermissionHandler {

    val permissionToRequest = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        permissionsTillApi28
    } else {
        permissionAfterApi28
    }

    fun checkPermissions(
        context: Context,
        activity: Activity
    ): PermissionResult {

        return when {
            permissionToRequest
                .map { permission ->
                    ContextCompat.checkSelfPermission(
                        context,
                        permission
                    )
                }
                .all { it == PackageManager.PERMISSION_GRANTED }
            -> PermissionResult.Granted

            permissionToRequest
                .map { permission ->
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        activity, permission
                    )
                }
                .any { it } -> PermissionResult.ShowRequestPermissionRationale

            else -> PermissionResult.Denied
        }
    }


    companion object {

        private val permissionsTillApi28 = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
        )

        private val permissionAfterApi28 = arrayOf(Manifest.permission.CAMERA)

    }
}


sealed interface PermissionResult {
    data object Granted : PermissionResult
    data object Denied : PermissionResult
    data object ShowRequestPermissionRationale : PermissionResult
}