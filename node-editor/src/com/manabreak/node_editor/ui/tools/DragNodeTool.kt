package com.manabreak.node_editor.ui.tools

import com.manabreak.node_editor.ui.NodeEditor
import com.manabreak.node_editor.ui.SwingUtils
import java.awt.Point
import java.awt.event.MouseEvent

class DragNodeTool(editor: NodeEditor) : Tool(editor) {
    var isDragging: Boolean = false

    val deltaMove = Point()

    val totalDelta = Point()

    private val updateLocation = Point()

    private val startLocation = Point()

    override fun onMouseDown(event: MouseEvent) {
        val localPoint = SwingUtils.screenToLocal(event.locationOnScreen, editor)
        beginDrag(localPoint)
    }

    override fun onMouseDrag(event: MouseEvent) {
        updateDrag(SwingUtils.screenToLocal(event.locationOnScreen, editor))
    }

    override fun onMouseUp(event: MouseEvent) {
        endDrag()
    }

    fun beginDrag(mouseLocation: Point) {
        isDragging = true
        startLocation.location = mouseLocation
        updateLocation.location = mouseLocation
    }

    fun endDrag() {
        isDragging = false
        deltaMove.setLocation(0, 0)
    }

    fun updateDrag(mouseLocation: Point) {
        if (!isDragging) {
            return
        }
        deltaMove.setLocation(mouseLocation.x - updateLocation.x, mouseLocation.y - updateLocation.y)
        totalDelta.setLocation(mouseLocation.x - startLocation.x, mouseLocation.y - startLocation.y)
        updateLocation.location = mouseLocation

        val temp = Point()
        for (selectedNode in editor.selection.selectedNodes) {
            selectedNode.getLocation(temp)
            temp.translate(deltaMove.x, deltaMove.y)
            selectedNode.location = temp
        }
    }
}