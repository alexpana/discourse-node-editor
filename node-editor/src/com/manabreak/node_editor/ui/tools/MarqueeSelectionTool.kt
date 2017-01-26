package com.manabreak.node_editor.ui.tools

import com.manabreak.node_editor.ui.NodeEditor
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.input.MouseEvent
import java.awt.Graphics

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
    }

    fun finishSelection() {
        rectangle = Rectangle2D.EMPTY
        from = Point2D.ZERO
        to = Point2D.ZERO
    }

    override fun paintOverNodes(g: Graphics) {
//        drawSelection(g, rectangle)
    }
}