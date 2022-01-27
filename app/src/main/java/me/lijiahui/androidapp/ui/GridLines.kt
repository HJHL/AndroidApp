package me.lijiahui.androidapp.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import me.lijiahui.androidapp.R

/**
 * 网格线
 * */
class GridLines(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {
    companion object {
        private const val TAG = "GridLines"
        private const val DEFAULT_LINE_WIDTH = R.dimen.grid_line_width
        private const val DEFAULT_LINE_COLOR = R.color.black
    }

    private val mPaint = Paint().apply {
        val defaultLineWidth = context.resources.getDimensionPixelSize(DEFAULT_LINE_WIDTH).toFloat()
        val defaultLineColor = DEFAULT_LINE_COLOR
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.GridLines)
        strokeWidth = typedArray.getDimension(R.styleable.GridLines_lineWidth, defaultLineWidth)
        color = typedArray.getColor(R.styleable.GridLines_lineColor, defaultLineColor)
        Log.d(TAG, "stroke width $strokeWidth color $color")
    }

    var mDrawBounds: RectF? = null
        set(value) {
            Log.d(TAG, "set new draw bounds: $field -> $value")
            field = value
            // need re-draw after set draw bounds
            invalidate()
        }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mDrawBounds?.let {
            // 绘制宽、高三等分网格
            val thirdWidth = it.width() / 3
            val thirdHeight = it.height() / 3
            for (i in 0..3) {
                val x = thirdWidth * i
                val y = thirdHeight * i
                // 绘制竖线
                canvas?.drawLine(it.left + x, it.top, it.left + x, it.bottom, mPaint)
                // 绘制横线
                canvas?.drawLine(it.left, it.top + y, it.right, it.top + y, mPaint)
            }
        }
    }
}