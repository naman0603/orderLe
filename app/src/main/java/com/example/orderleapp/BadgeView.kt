package com.example.orderleapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class BadgeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val badgeBounds = RectF()
    private var badgeBackgroundColor = Color.RED

    init {
        badgePaint.color = badgeBackgroundColor
        gravity = android.view.Gravity.CENTER
        setTextColor(Color.WHITE)
        setTextSize(12f)
        updateBadgeVisibility(false)
    }

    fun setBadgeBackgroundColor(color: Int) {
        badgeBackgroundColor = color
        badgePaint.color = badgeBackgroundColor
        invalidate()
    }

    fun setBadgeCount(count: Int) {
        text = if (count <= 0) "" else count.toString()
        updateBadgeVisibility(count > 0)
    }

    private fun updateBadgeVisibility(visible: Boolean) {
        visibility = if (visible) VISIBLE else GONE
    }

    override fun onDraw(canvas: Canvas) {
        if (text.isEmpty()) return
        val textWidth = paint.measureText(text.toString())
        val badgeRadius = (textWidth / 2) + 10 // Adjust badge size as needed
        badgeBounds.set(
            width - badgeRadius * 2,
            0f,
            width.toFloat(),
            badgeRadius * 2
        )
        canvas.drawRoundRect(badgeBounds, badgeRadius, badgeRadius, badgePaint)
        super.onDraw(canvas)
    }
}
