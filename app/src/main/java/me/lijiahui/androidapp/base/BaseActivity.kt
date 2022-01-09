package me.lijiahui.androidapp.base

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "BaseActivity"
        private val ACTIVITY_NAME = ::BaseActivity.javaClass.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate $ACTIVITY_NAME")
        super.onCreate(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        Log.d(TAG, "onCreate2 $ACTIVITY_NAME")
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onStart() {
        Log.d(TAG, "onStart $ACTIVITY_NAME")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume $ACTIVITY_NAME")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause $ACTIVITY_NAME")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop $ACTIVITY_NAME")
        super.onStop()
    }

    override fun onRestart() {
        Log.d(TAG, "onRestart $ACTIVITY_NAME")
        super.onRestart()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy $ACTIVITY_NAME")
        super.onDestroy()
    }
}