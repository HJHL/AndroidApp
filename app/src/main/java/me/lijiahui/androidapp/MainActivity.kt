package me.lijiahui.androidapp

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebViewClient
import android.widget.Toast
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.lijiahui.androidapp.base.BaseActivity
import me.lijiahui.androidapp.databinding.ActivityMainBinding
import java.io.File

class MainActivity : BaseActivity() {

    companion object {
        private const val TAG = "MainActivity"
        const val URL = "https://www.baidu.com"
        private const val VERTEX_CODE_FILE_NAME = "custom_vertex_code.glsl"
        private const val FRAGMENT_CODE_FILE_NAME = "custom_fragment_code.glsl"
        private const val WALL_FILE_NAME = "wall.jpeg"
        private const val FACE_FILE_NAME = "awesomeface.png"
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
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }
    }

    /**
     * 初始化各控件的事件监听
     * */
    private fun initListener() {
        mBinding.mrb.setOnClickListener {
//            mBinding.web.loadUrl(URL)
            copyAssetsToFileDir(true)
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

    private fun copyAssetsToFileDir(forceUpdate: Boolean = false) {
        // 拷贝 shader 代码到 /data/data/<package-name>/files/ 目录下
        // TODO：找到更好的方式存放 shader 代码
        GlobalScope.launch(Dispatchers.IO) {
            Log.d(TAG, "force update? $forceUpdate")
            val vertexFile = File(filesDir, VERTEX_CODE_FILE_NAME)
            val fragmentFile = File(filesDir, FRAGMENT_CODE_FILE_NAME)
            val wallFile = File(filesDir, WALL_FILE_NAME)
            val faceFile = File(filesDir, FACE_FILE_NAME)
            if (!vertexFile.exists() || forceUpdate) {
                vertexFile.writeBytes(assets.open(VERTEX_CODE_FILE_NAME).readBytes())
                Log.i(TAG, "write vertex file success")
            }
            if (!fragmentFile.exists() || forceUpdate) {
                fragmentFile.writeBytes(assets.open(FRAGMENT_CODE_FILE_NAME).readBytes())
                Log.i(TAG, "write fragment file success")
            }
            if (!wallFile.exists() || forceUpdate) {
                wallFile.writeBytes(assets.open(WALL_FILE_NAME).readBytes())
                Log.i(TAG, "write wall file success")
            }
            if (!faceFile.exists() || forceUpdate) {
                faceFile.writeBytes(assets.open(FACE_FILE_NAME).readBytes())
                Log.i(TAG, "write face file success")
            }
            mainExecutor.execute {
                Toast.makeText(MyApplication.app, "All files copied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun doAfterCreate() {
        // 调试方便，Activity 创建后就将文件拷贝到目录
        copyAssetsToFileDir(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initView()
        initListener()
        doAfterCreate()
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