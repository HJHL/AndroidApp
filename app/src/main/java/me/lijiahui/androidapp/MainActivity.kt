package me.lijiahui.androidapp

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebViewClient
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import me.lijiahui.androidapp.base.BaseActivity
import me.lijiahui.androidapp.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    companion object {
        private const val TAG = "MainActivity"
        const val URL = "https://www.baidu.com"
    }

    private var _binding: ActivityMainBinding? = null

    private val mBinding get() = _binding!!

    private val referrerClient by lazy {
        InstallReferrerClient.newBuilder(applicationContext).build()
    }

    /**
     * 初始化 ui
     * */
    private fun initView() {
        mBinding.web.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            Log.d(TAG, "js enabled ${settings.javaScriptEnabled}")
        }
        mBinding.surfaceView.apply {
            renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        }
    }

    /**
     * 初始化各控件的事件监听
     * */
    private fun initListener() {
        mBinding.mrb.setOnClickListener {
            mBinding.web.loadUrl(URL)
        }
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        Log.d(TAG, "Google Play connection established")
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        Log.d(TAG, "current Google Play do not support this method")
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        Log.d(TAG, "Google Play service not available")
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                Log.d(TAG, "Google Play connection disconnect")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initView()
        initListener()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    val webview = mBinding.web
                    if (webview.canGoBack()) {
                        webview.goBack()
                        return true
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}