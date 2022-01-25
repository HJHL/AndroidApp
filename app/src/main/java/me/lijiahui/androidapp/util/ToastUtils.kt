package me.lijiahui.androidapp.util

import android.widget.Toast
import androidx.annotation.StringRes
import me.lijiahui.androidapp.MyApplication

object ToastUtils {
    fun showShort(msg: String) {
        Toast.makeText(MyApplication.app, msg, Toast.LENGTH_SHORT).show()
    }

    fun showShort(@StringRes stringRes: Int) {
        showShort(MyApplication.app.getString(stringRes))
    }
}