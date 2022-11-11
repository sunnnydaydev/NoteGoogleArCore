package com.example.notegooglearcore

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notegooglearcore.helpers.ARCoreSessionLifecycleHelper
import com.example.notegooglearcore.helpers.CameraPermissionHelper
import com.google.ar.core.CameraConfig
import com.google.ar.core.CameraConfigFilter
import com.google.ar.core.Session
import timber.log.Timber
import java.util.*


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

        })
        // 设置相机
        val filter = CameraConfigFilter(session)
        //过滤出不使用深度的
        filter.depthSensorUsage = EnumSet.of(CameraConfig.DepthSensorUsage.DO_NOT_USE)
        //过滤出相机拍摄帧速率为30fps的
        filter.targetFps = EnumSet.of(CameraConfig.TargetFps.TARGET_FPS_30)

        // 选第零个元素，因为这个是最符合筛选条件的cameraConfig
        val cameraConfig = session.getSupportedCameraConfigs(filter)[0]
        session.cameraConfig = cameraConfig
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
            // 为true 代表需要提醒用户，为false 代表不需要提示用户
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                CameraPermissionHelper.launchPermissionSetting(this)
            }
            finish()
        }else{
            Timber.d("has Permission~")
        }
    }
}