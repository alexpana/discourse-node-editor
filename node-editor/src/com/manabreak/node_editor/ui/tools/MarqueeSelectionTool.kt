package com.manabreak.node_editor.ui.tools

import com.manabreak.node_editor.ui.NodeEditor
import com.manabreak.node_editor.ui.Renderer.drawSelection
import java.awt.Graphics
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.MouseEvent

class MarqueeSelectionTool(editor: NodeEditor) : Tool(editor = editor) {
    val from = Point()

    val to = Point()

    val rectangle = Rectangle()

    override fun onMouseDown(event: MouseEvent) {
//        val localPoint = SwingUtils.screenToLocal(event.locationOnScreen, editor)
//        beginSelection(localPoint)
    }

    override fun onMouseDrag(event: MouseEvent) {
//        val localPoint = SwingUtils.screenToLocal(event.locationOnScreen, editor)
//        update(localPoint)
        editor.selection.selectFromMarquee(rectangle, editor.nodes)
    }

    override fun onMouseUp(event: MouseEvent) {
        finishSelection()
    }

    fun beginSelection(from: Point) {
        this.from.location = from
    }

    fun update(update: Point) {
        to.location = update
        val x = Math.min(from.x, to.x)
        val y = Math.min(from.y, to.y)
        val width = Math.abs(to.x - from.x)
        val height = Math.abs(to.y - from.y)
        rectangle.setBounds(x, y, width, height)
    }

    fun finishSelection() {
        rectangle.setRect(0.0, 0.0, 0.0, 0.0)
        from.setLocation(0, 0)
        to.setLocation(0, 0)
    }

    override fun paintOverNodes(g: Graphics) {
        drawSelection(g, rectangle)
    }
}