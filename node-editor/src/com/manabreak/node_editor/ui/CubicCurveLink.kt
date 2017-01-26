package com.manabreak.node_editor.ui

import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.CubicCurve

class CubicCurveLink {
    val backgroundCurve: CubicCurve = CubicCurve()
    val foregroundCurve: CubicCurve = CubicCurve()

    init {
        backgroundCurve.strokeWidth = 5.0
        backgroundCurve.stroke = Theme.theme.nodeBorderColor
        backgroundCurve.fill = Color.TRANSPARENT

        foregroundCurve.strokeWidth = 1.0
        foregroundCurve.stroke = Theme.theme.linkColor
        foregroundCurve.fill = Color.TRANSPARENT
    }

    fun update(from: Point2D, to: Point2D) {
        update(from.x, from.y, to.x, to.y)
    }

    fun update(x1: Double, y1: Double, x2: Double, y2: Double) {
        updateCurve(backgroundCurve, x1, y1, x2, y2)
        updateCurve(foregroundCurve, x1, y1, x2, y2)
    }

    private fun updateCurve(curve: CubicCurve, x1: Double, y1: Double, x2: Double, y2: Double) {
        val horizontalDistance = Math.abs(x1 - x2)
        val handleOffset = Math.max(horizontalDistance / 2, 20.0)

        curve.startX = x1
        curve.startY = y1
        curve.controlX1 = x1 + handleOffset
        curve.controlY1 = y1
        curve.controlX2 = x2 - handleOffset
        curve.controlY2 = y2
        curve.endX = x2
        curve.endY = y2
    }
}