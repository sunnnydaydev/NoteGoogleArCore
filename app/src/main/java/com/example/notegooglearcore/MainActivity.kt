package com.example.notegooglearcore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notegooglearcore.helpers.ARCoreSessionLifecycleHelper
import com.example.notegooglearcore.helpers.CameraPermissionHelper
import com.google.ar.core.Session
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    private lateinit var arCoreSessionLifecycleHelper: ARCoreSessionLifecycleHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initArCoreSessionLifecycleHelper()

    }


    private fun initArCoreSessionLifecycleHelper() {
        arCoreSessionLifecycleHelper = ARCoreSessionLifecycleHelper(this)
        arCoreSessionLifecycleHelper.exceptionCallBack = {
         Timber.d("an error occurred:$it")
        }
        arCoreSessionLifecycleHelper.beforeSessionResume = ::configSession
        lifecycle.addObserver(arCoreSessionLifecycleHelper)
    }

    private fun configSession(session: Session) {
        session.configure(session.config.apply {
         // todo 进行配置
        })
    }

    /**
     * 权限回调
     * */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 无camera权限时做三件事：
        // 1、toast提示
        // 2、跳转权限设置页面
        // 3、关闭app
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(
                this,
                "Camera permission is needed to run this application",
                Toast.LENGTH_LONG
            ).show()
            // 无权限&shouldShowRequestPermissionRationale为false则代表用户点击了"拒接不再询问"
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                CameraPermissionHelper.launchPermissionSetting(this)
            }
            finish()
        }else{
            Timber.d("has Permission~")
        }
    }
}