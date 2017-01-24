package com.manabreak.node_editor.ui

import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.scene.transform.Affine
import javafx.scene.transform.Translate
import java.awt.Point

class Grid : Canvas() {

    val size = 30

    @Suppress("unused")
    fun snap(point: Point): Point {
        return Point((point.getX() / size.toFloat()).toInt() * size, (point.getY() / size.toFloat()).toInt() * size)
    }

    @Suppress("UNUSED_PARAMETER")
    fun repaint() {
        val firstVisibleLine = 0
        val firstVisibleColumn = 0

        val g2d = graphicsContext2D

        val minor = Color.web("#424548")
        val major = Color.web("#464A4D")

        g2d.lineWidth = 0.5
        g2d.transform = Affine(Translate(0.5, 0.5))

        g2d.fill = Color.web("#3c3f41")
        g2d.rect(0.0, 0.0, width, height)

        run {
            var l = 0
            while (firstVisibleLine + l * size < height) {
                val lineY = (firstVisibleLine + l * size).toDouble()
                g2d.stroke = if (l % 4 == 0) major else minor
                g2d.strokeLine(0.0, lineY, width, lineY)
                l += 1
            }
        }
        run {
            var c = 0
            while (firstVisibleColumn + c * size < width) {
                val lineX = (firstVisibleColumn + c * size).toDouble()
                g2d.stroke = if (c % 4 == 0) major else minor
                g2d.strokeLine(lineX, 0.0, lineX, height)
                c += 1
            }
        }
    }
}
