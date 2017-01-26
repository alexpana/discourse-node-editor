package com.manabreak.node_editor.ui.tools

import com.manabreak.node_editor.ui.NodeEditor
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color

class MarqueeSelectionTool(editor: NodeEditor) : Tool(editor = editor) {
    var from = Point2D(0.0, 0.0)

    var to = Point2D(0.0, 0.0)

    var rectangle = Rectangle2D.EMPTY

    override fun onMouseDown(event: MouseEvent) {
        beginSelection(Point2D(event.x, event.y))
    }

    override fun onMouseDrag(event: MouseEvent) {
        update(Point2D(event.x, event.y))
        editor.selection.selectFromMarquee(rectangle, editor.nodes)
    }

    override fun onMouseUp(event: MouseEvent) {
        finishSelection()
    }

    fun beginSelection(from: Point2D) {
        this.from = from
    }

    fun update(update: Point2D) {
        to = update
        val x = Math.min(from.x, to.x)
        val y = Math.min(from.y, to.y)
        val width = Math.abs(to.x - from.x)
        val height = Math.abs(to.y - from.y)
        rectangle = Rectangle2D(x, y, width, height)
        val g2d = editor.foregroundCanvas.graphicsContext2D
        g2d.clearRect(0.0, 0.0, editor.foregroundCanvas.width, editor.foregroundCanvas.height)
        g2d.fill = Color.web("#ffffff", 0.1)
        g2d.fillRect(x, y, width, height)
        g2d.stroke = Color.web("#ffffff", 0.5)
        g2d.setLineDashes(4.0, 4.0)
        g2d.strokeRect(x, y, width, height)
    }

    fun finishSelection() {
        val g2d = editor.foregroundCanvas.graphicsContext2D
        g2d.clearRect(0.0, 0.0, editor.foregroundCanvas.width, editor.foregroundCanvas.height)
        rectangle = Rectangle2D.EMPTY
        from = Point2D.ZERO
        to = Point2D.ZERO
    }
}