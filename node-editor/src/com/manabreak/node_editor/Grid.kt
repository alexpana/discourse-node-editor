package com.manabreak.node_editor

import com.manabreak.node_editor.Theme.Companion.theme
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point

class Grid {

    val size = 30

    fun snap(point: Point): Point {
        return Point((point.getX() / size.toFloat()).toInt() * size, (point.getY() / size.toFloat()).toInt() * size)
    }

    fun paint(g: Graphics, x: Int, y: Int, w: Int, h: Int) {
        val firstVisibleLine = 0
        val firstVisibleColumn = 0

        val g2d = g.create() as Graphics2D

        g2d.color = theme.gridColor
        run {
            var l = 0
            while (firstVisibleLine + l * size < h) {
                val lineY = firstVisibleLine + l * size
                g2d.drawLine(0, lineY, w, lineY)
                l += 1
            }
        }
        run {
            var c = 0
            while (firstVisibleColumn + c * size < w) {
                val lineX = firstVisibleColumn + c * size
                g2d.drawLine(lineX, 0, lineX, h)
                c += 1
            }
        }

        g2d.color = theme.gridMajorColor
        var l = 0
        while (firstVisibleLine + l * size < h) {
            val lineY = firstVisibleLine + l * size
            g2d.drawLine(0, lineY, w, lineY)
            l += 4
        }
        var c = 0
        while (firstVisibleColumn + c * size < w) {
            val lineX = firstVisibleColumn + c * size
            g2d.drawLine(lineX, 0, lineX, h)
            c += 4
        }

        g2d.dispose()
    }
}
