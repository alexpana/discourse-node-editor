package com.manabreak.node_editor.ui

import com.manabreak.node_editor.ui.tools.Tool
import java.awt.Point

class DragHelper(editor: NodeEditor) : Tool(editor) {

    var isDragging: Boolean = false

    val deltaMove = Point()

    val totalDelta = Point()

    private val updateLocation = Point()

    private val startLocation = Point()


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
    }
}
