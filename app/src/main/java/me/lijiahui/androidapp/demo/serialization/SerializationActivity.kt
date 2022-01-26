package me.lijiahui.androidapp.demo.serialization

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.lijiahui.androidapp.databinding.ActivitySerializationBinding

class SerializationActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "SerializationActivity"
    }

    private var _binding: ActivitySerializationBinding? = null
    private val mBinding get() = _binding!!

    private val mSerializationActivityViewModel: SerializationActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySerializationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initView()
    }

    private fun initView() {
        mBinding.requestUsers.apply {
            setOnClickListener { requestUsers() }
        }
        mBinding.nextUser.apply {
            setOnClickListener { requestNextUser() }
        }
    }

    private fun requestUsers() = lifecycleScope.launch(Dispatchers.IO) {
        Log.d(TAG, "requestUsers")
        mSerializationActivityViewModel.requestUsers()
    }

    private fun requestNextUser() = lifecycleScope.launch(Dispatchers.IO) {
        Log.d(TAG, "requestNextUser")
//        mSerializationActivityViewModel.requestNextUser()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}