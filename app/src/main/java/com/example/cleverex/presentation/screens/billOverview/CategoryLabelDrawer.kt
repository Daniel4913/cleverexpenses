package com.example.cleverex.presentation.screens.billOverview

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import com.github.tehras.charts.piechart.utils.toLegacyInt

class CategoryLabelDrawer : com.github.tehras.charts.bar.renderer.label.LabelDrawer {
    private val leftOffset = 50f

    private val paint = android.graphics.Paint().apply {
        textAlign = android.graphics.Paint.Align.CENTER
        color = Color.Blue.toLegacyInt()
        textSize = 42f
    }

    override fun drawLabel(
        drawScope: DrawScope,
        canvas: Canvas,
        label: String,
        barArea: Rect,
        xAxisArea: Rect
    ) {
        canvas.nativeCanvas.drawText(
            label,
            barArea.left + leftOffset,
            barArea.bottom + 65f,
            paint
        )
    }
}
