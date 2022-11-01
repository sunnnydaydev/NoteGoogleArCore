package com.example.notegooglearcore.helpers

import android.app.Activity
import android.os.Handler
import com.google.ar.core.ArCoreApk
import timber.log.Timber
import java.lang.Exception

/**
 * Create by SunnyDay /11/01 15:15:32
 */
object CheckerUtil {
    /**
     * 检测当前的设备是否支持ar
     * */
     fun checkDeviceIsSupportForAR(activity:Activity): Boolean {
        val availability = ArCoreApk.getInstance().checkAvailability(activity)
        if (availability.isTransient) {
            Handler().postDelayed({
                checkDeviceIsSupportForAR(activity)
            }, 200)
        }
        val result = availability.isSupported
        Timber.d("当前设备是否支持ar:$result")
        return result
    }

     fun checkIsInstalledArGooglePlayService(activity:Activity){
        try {
            when(ArCoreApk.getInstance().requestInstall(activity,true)){
                ArCoreApk.InstallStatus.INSTALLED -> {
                    Timber.d("ArCoreApk.InstallStatus.INSTALLED")
                }
                ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                    // When this method returns `INSTALL_REQUESTED`:
                    // 1. ARCore pauses this activity.
                    // 2. ARCore prompts the user to install or update Google Play
                    //    Services for AR (market://details?id=com.google.ar.core).
                    // 3. ARCore downloads the latest device profile data.
                    // 4. ARCore resumes this activity. The next invocation of
                    //    requestInstall() will either return `INSTALLED` or throw an
                    //    exception if the installation or update did not succeed.
                    Timber.d("ArCoreApk.InstallStatus.INSTALL_REQUESTED")
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
            Timber.d("update or download ar core failure. error msg:${e.message}")
        }
    }
}