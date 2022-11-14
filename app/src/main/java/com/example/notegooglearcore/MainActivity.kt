package com.example.notegooglearcore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notegooglearcore.helpers.ARCoreSessionLifecycleHelper
import com.example.notegooglearcore.helpers.CameraPermissionHelper
import com.google.ar.core.RecordingConfig
import com.google.ar.core.Session
import com.google.ar.core.exceptions.RecordingFailedException
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init.setOnClickListener {
            startActivity(Intent(this,InitActivity::class.java))
        }
        recordPlayBack.setOnClickListener {
            startActivity(Intent(this,RecordPlayBackActivity::class.java))
        }
    }
}