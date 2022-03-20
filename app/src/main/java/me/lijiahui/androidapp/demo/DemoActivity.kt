package me.lijiahui.androidapp.demo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import me.lijiahui.androidapp.R
import me.lijiahui.androidapp.databinding.ActivityDemoBinding
import me.lijiahui.androidapp.demo.compose.ComposeActivity
import me.lijiahui.androidapp.demo.exoplayer.ExoFragment
import me.lijiahui.androidapp.demo.ffmpeg.FfmpegFragment
import me.lijiahui.androidapp.demo.floating.FloatingFragment
import me.lijiahui.androidapp.demo.media_codec.MediaCodecFragment
import me.lijiahui.androidapp.demo.screen_recorder.ScreenRecorderFragment

class DemoActivity : AppCompatActivity() {
    private val mBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityDemoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_demo, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        translateTo(item.title, item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun translateTo(title: CharSequence, menuId: Int) {
        if (menuId == R.id.menu_id_compose) {
            startActivity(Intent(this, ComposeActivity::class.java))
            finish()
        }
        setTitle(title)
        when (menuId) {
            R.id.menu_id_exo -> {
                ExoFragment()
            }
            R.id.menu_id_ffmpeg -> {
                FfmpegFragment()
            }
            R.id.menu_id_screen_recorder -> {
                ScreenRecorderFragment()
            }
            R.id.menu_id_floating_window -> {
                FloatingFragment()
            }
            R.id.menu_id_mediacodec -> {
                MediaCodecFragment()
            }
            else -> null
        }?.let {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, it).commit()
        }
    }
}