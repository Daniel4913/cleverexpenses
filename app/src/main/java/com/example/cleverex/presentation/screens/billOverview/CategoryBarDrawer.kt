package com.example.cleverex.presentation.screens.billOverview

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.github.tehras.charts.bar.BarChartData

class CategoryBarDrawer : com.github.tehras.charts.bar.renderer.bar.BarDrawer {
    private val barPaint = Paint().apply {
        isAntiAlias = true
    }

    override fun drawBar(
        drawScope: DrawScope,
        canvas: Canvas,
        barArea: Rect,
        bar: BarChartData.Bar
    ) {
        canvas.drawRoundRect(
            barArea.left,
            barArea.top,
            barArea.right,
            barArea.bottom,
            16f,
            16f,
            barPaint.apply {
                color = bar.color
            }
        )
    }
}