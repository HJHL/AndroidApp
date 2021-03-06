package me.lijiahui.androidapp.demo.screen_recorder

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ScreenRecorderService : Service() {
    companion object {
        private const val TAG = "ScreenRecorderService"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind")
        return null
    }

}