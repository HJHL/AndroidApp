package me.lijiahui.androidapp.demo.floating

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.util.Log

object FloatingWindowsManager {
    private const val TAG = "FloatingWindowsManager"
    const val OP_SYSTEM_ALERT_WINDOW = 24

    @SuppressLint("ObsoleteSdkInt")
    fun checkFloatingWindowPermission(context: Context): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            val opsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val clazz = AppOpsManager::class.java
            kotlin.runCatching {
                val method = clazz.getDeclaredMethod(
                    "checkOp",
                    Int::class.java,
                    Int::class.java,
                    String::class.java
                )
                return method.invoke(
                    opsManager,
                    OP_SYSTEM_ALERT_WINDOW,
                    Process.myUid(),
                    context.packageName
                ) == AppOpsManager.MODE_ALLOWED
            }.onFailure {
                Log.w(TAG, "invoke checkOp failed: ${it.message}")
            }
            true
        }

    @SuppressLint("QueryPermissionsNeeded")
    fun requestFloatingPermission(context: Context) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package: " + context.packageName)
        )
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intent.resolveActivity(context.packageManager).let {
            Log.d(TAG, "${it.packageName} $it")
            context.startActivity(intent)
        }
    }
}