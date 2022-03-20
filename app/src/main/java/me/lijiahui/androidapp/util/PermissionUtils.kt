package me.lijiahui.androidapp.util

import android.content.pm.PackageManager
import me.lijiahui.androidapp.MyApplication

object PermissionUtils {
    fun checkPermissions(permissions: Array<String>) = permissions.all {
        MyApplication.app.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
    }
}