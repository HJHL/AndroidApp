package me.lijiahui.androidapp.demo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import me.lijiahui.androidapp.R
import me.lijiahui.androidapp.databinding.ActivityDemoBinding
import me.lijiahui.androidapp.demo.exoplayer.ExoFragment

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
        setTitle(title)
        when (menuId) {
            R.id.menu_id_exo -> {
                ExoFragment()
            }
            else -> null
        }?.let {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, it).commit()
        }
    }
}