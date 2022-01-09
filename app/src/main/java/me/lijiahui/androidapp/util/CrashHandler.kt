package me.lijiahui.androidapp.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import me.lijiahui.androidapp.MyApplication
import kotlin.system.exitProcess

object CrashHandler : Thread.UncaughtExceptionHandler {

    private const val TIME_WAIT_FOR_EXIT = 3000L
    private const val TAG = "CrashHandler"
    private const val KEY_APP_VERSION_NAME = "app_version_name"
    private const val KEY_APP_VERSION_CODE = "app_version_code"
    private const val KEY_OS_VERSION_CODE = "os_version_code"
    private const val KEY_OS_VERSION_NAME = "os_version_name"
    private const val KEY_OS_INFO = "os_info"

    private val mCollectInfoMap: HashMap<String, String> = HashMap<String, String>()

    private val mDefaultHandler by lazy {
        Thread.getDefaultUncaughtExceptionHandler()
    }

    fun init() {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        if (!handleException(e)) {
            mDefaultHandler.uncaughtException(t, e);
        } else {
            try {
                Thread.sleep(TIME_WAIT_FOR_EXIT);
            } catch (e: InterruptedException) {
                Log.e(TAG, "${e.message}")
            }
            OSUtils.killSelf()
            exitProcess(1);
        }
    }

    private fun handleException(e: Throwable): Boolean {
        collectBaseInfo(MyApplication.app)
        saveLogToFile(e)
        return true;
    }

    private fun collectBaseInfo(ctx: Context) {
        // collect package info
        try {
            val pm = ctx.packageManager
            val packageInfo = pm.getPackageInfo(ctx.packageName, PackageManager.GET_ACTIVITIES)
            val versionCode: String = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toString()
            } else {
                packageInfo.versionCode.toString()
            }
            mCollectInfoMap[KEY_APP_VERSION_NAME] = packageInfo.versionName
            mCollectInfoMap[KEY_APP_VERSION_CODE] = versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "${e.message}")
        }
        val clazz = Build::class.java
        for (field in clazz.declaredFields) {
            try {
                field.isAccessible = true
                mCollectInfoMap[field.name] = field.get(null)?.toString() ?: ""
            } catch (e: Exception) {
                // ignore
            }
        }
    }

    private fun saveLogToFile(e: Throwable) {
    }
}