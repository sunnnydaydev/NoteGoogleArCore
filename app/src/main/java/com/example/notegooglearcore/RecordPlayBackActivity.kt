package com.example.notegooglearcore


import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notegooglearcore.helpers.CameraPermissionHelper
import com.example.notegooglearcore.helpers.CheckerUtil
import com.google.ar.core.RecordingConfig
import com.google.ar.core.Session
import kotlinx.android.synthetic.main.activity_record_play_back.*
import timber.log.Timber
import java.io.File

class RecordPlayBackActivity : AppCompatActivity() {

    private var session: Session? = null


    override fun onResume() {
        super.onResume()
        if (!CheckerUtil.checkDeviceIsSupportForAR(this)){
            finish()
        }
        if (!CheckerUtil.checkIsInstalledArGooglePlayService(this)){
            return
        }
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this)
        }else{
            // do something
            session = Session(this)
            session?.resume()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_play_back)

        start.setOnClickListener {
            val destination = Uri.fromFile(File(filesDir, "recording.mp4"))
            val recordingConfig = RecordingConfig(session)
                .setMp4DatasetUri(destination)
                .setAutoStopOnPause(true)
            session?.startRecording(recordingConfig)
        }

        stop.setOnClickListener {
            session?.stopRecording()
        }

        play.setOnClickListener {
            session?.pause()
            session?.setPlaybackDatasetUri(Uri.fromFile(File(filesDir, "recording.mp4")))
            session?.resume()
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(
                this,
                "Camera permission is needed to run this application",
                Toast.LENGTH_LONG
            ).show()
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                CameraPermissionHelper.launchPermissionSetting(this)
            }
            finish()
        }else{
            Timber.d("has Permission~")
            //do something
            session = Session(this)
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
        session?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
        session?.close()
        session=null
    }

}