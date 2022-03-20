package me.lijiahui.androidapp.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceView
import kotlin.math.roundToInt

class AutoFitSurfaceView(context: Context, attributeSet: AttributeSet?) :
    SurfaceView(context, attributeSet) {

    companion object {
        private const val TAG = "AutoFitSurfaceView"
    }

    private var aspectRatio = 0f

    fun setAspectRatio(width: Int, height: Int) {
        require(width > 0 && height > 0) {
            Log.d(TAG, "input size must greater than 0")
        }
        aspectRatio = width.toFloat() / height.toFloat()
        holder.setFixedSize(width, height)
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (aspectRatio == 0f) {
            Log.d(TAG, "use default dimension: ${width}x$height")
            setMeasuredDimension(width, height)
        } else {
            val newWidth: Int
            val newHeight: Int
            val actualRatio = if (width > height) aspectRatio else 1f / aspectRatio
            if (width < height * actualRatio) {
                newWidth = height
                newHeight = (height * actualRatio).roundToInt()
            } else {
                newWidth = width
                newHeight = (width / actualRatio).roundToInt()
            }
            Log.d(TAG, "calculate dimension ${newWidth}x$newHeight")
            setMeasuredDimension(newWidth, newHeight)
        }
    }
}