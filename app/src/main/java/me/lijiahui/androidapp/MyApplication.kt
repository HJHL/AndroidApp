package me.lijiahui.androidapp

import android.app.Application
import me.lijiahui.androidapp.util.CrashHandler

class MyApplication : Application() {
    companion object {
        lateinit var app: Application
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        CrashHandler.init()
    }
}