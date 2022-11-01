package com.example.notegooglearcore.helpers

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Create by SunnyDay /11/01 11:58:56
 */
object CameraPermissionHelper {
    private const val CAMERA_PERMISSION_CODE = 0
    private const val CAMERA_PERMISSION = Manifest.permission.CAMERA

    fun hasCameraPermission(activity: Activity) = ContextCompat.checkSelfPermission(
        activity,
        CAMERA_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    fun requestCameraPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(CAMERA_PERMISSION),
            CAMERA_PERMISSION_CODE
        )
    }

    fun shouldShowRequestPermissionRationale(activity: Activity): Boolean =
        ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            CAMERA_PERMISSION
        )

    fun launchPermissionSetting(activity: Activity) {
        activity.startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", activity.packageName, null)
        })
    }
}