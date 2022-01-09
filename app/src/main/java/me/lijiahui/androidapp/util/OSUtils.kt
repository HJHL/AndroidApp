package me.lijiahui.androidapp.util

import android.os.Process

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
}