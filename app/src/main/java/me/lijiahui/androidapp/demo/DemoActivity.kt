package me.lijiahui.androidapp.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.lijiahui.androidapp.databinding.ActivityDemoBinding

class DemoActivity : AppCompatActivity() {
    private val mBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityDemoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }
}