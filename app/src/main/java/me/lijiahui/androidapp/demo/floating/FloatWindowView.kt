package me.lijiahui.androidapp.demo.floating

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import me.lijiahui.androidapp.R

class FloatWindowView(context: Context, attributeSet: AttributeSet? = null) :
    FrameLayout(context, attributeSet) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_floating_window, this, true)
    }
}