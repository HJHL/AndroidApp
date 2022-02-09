package me.lijiahui.androidapp.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.util.Size
import android.view.WindowManager

object OSUtils {

    @JvmStatic
    fun killSelf() {
        val pid = Process.myPid();
        killProcess(pid);
    }

    @JvmStatic
    fun killProcess(pid: Int) {
        Process.killProcess(pid);
    }

    @JvmStatic
    fun openDeveloperMode(context: Context) {
        Intent(
            Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
            Uri.parse("package: ${context.packageName}")
        ).let {
            context.startActivity(it)
        }
    }
}