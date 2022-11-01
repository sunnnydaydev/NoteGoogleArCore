package com.example.notegooglearcore.helpers

import android.app.Activity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Session
import timber.log.Timber
import kotlin.Exception

/**
 * Create by SunnyDay /11/01 14:29:19
 */
class ARCoreSessionLifecycleHelper(
    private val activity: Activity,
    private val features: Set<Session.Feature> = setOf()
) : DefaultLifecycleObserver {
    var requestInstall = false
    var session: Session? = null
        private set

    var exceptionCallBack:((Exception)->Unit)? = null
    var beforeSessionResume:((Session)->Unit)? = null


    private fun tryCreateSession(): Session? {
        //首先去检测权限
        if (!CameraPermissionHelper.hasCameraPermission(activity)) {
            CameraPermissionHelper.requestCameraPermission(activity)
            return null
        }
        // 创建session
        return try {
            when(ArCoreApk.getInstance().requestInstall(activity,!requestInstall)){
                ArCoreApk.InstallStatus.INSTALL_REQUESTED->{
                    requestInstall = true
                    return null
                }
                ArCoreApk.InstallStatus.INSTALLED->{
                    // todo nothing. in fact this case need not write.
                    // when code run here,and go below continue.
                }
            }
            Session(activity,features)
        } catch (e: Exception) {
            exceptionCallBack?.invoke(e)
            null
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        Timber.d("onResume")
        val session = this.session ?: tryCreateSession() ?:return
        try {
            beforeSessionResume?.invoke(session)
            session.resume()
            this.session = session
        }catch (e:Exception){
             exceptionCallBack?.invoke(e)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        Timber.d("onPause")
        session?.pause()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Timber.d("onDestroy")
        session?.close()
        session=null
    }
}