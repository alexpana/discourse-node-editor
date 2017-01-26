package com.manabreak.node_editor.ui.tools

import com.manabreak.node_editor.ui.NodeEditor
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent

class DragNodeTool(editor: NodeEditor) : Tool(editor) {
    var isDragging: Boolean = false

    var deltaMove = Point2D.ZERO

    var totalDelta = Point2D.ZERO

    private var updateLocation = Point2D.ZERO

    private var startLocation = Point2D.ZERO

    override fun onMouseDown(event: MouseEvent) {
        beginDrag(Point2D(event.x, event.y))
    }

    override fun onMouseDrag(event: MouseEvent) {
        updateDrag(Point2D(event.x, event.y))
    }

    override fun onMouseUp(event: MouseEvent) {
        endDrag()
    }

    fun beginDrag(mouseLocation: Point2D) {
        isDragging = true
        startLocation = mouseLocation
        updateLocation = mouseLocation
    }

    fun endDrag() {
        isDragging = false
        deltaMove = Point2D.ZERO
    }

    fun updateDrag(mouseLocation: Point2D) {
        if (!isDragging) {
            return
        }
        deltaMove = Point2D(mouseLocation.x - updateLocation.x, mouseLocation.y - updateLocation.y)
        totalDelta = Point2D(mouseLocation.x - startLocation.x, mouseLocation.y - startLocation.y)
        updateLocation = mouseLocation

        for (selectedNode in editor.selection.selectedNodes) {
            selectedNode.relocate(selectedNode.layoutX + deltaMove.x, selectedNode.layoutY + deltaMove.y)
        }
    }
}