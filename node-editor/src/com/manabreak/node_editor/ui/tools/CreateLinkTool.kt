package com.manabreak.node_editor.ui.tools

import com.manabreak.node_editor.model.Slot
import com.manabreak.node_editor.ui.NodeEditor
import com.manabreak.node_editor.ui.SlotComponent
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent

class CreateLinkTool(editor: NodeEditor) : Tool(editor = editor) {

    var isConnecting = false

    var from: Slot? = null

    var hoveredComponent: SlotComponent? = null

    // Endpoints used for rendering the link
    val fromLocation = Point2D.ZERO
    val toLocation = Point2D.ZERO

    override fun onMouseDown(event: MouseEvent) {
//        val localPoint = SwingUtils.screenToLocal(event.locationOnScreen, editor)
//        val componentUnderCursor = SwingUtils.getComponentAt(editor, localPoint) as SlotComponent
//
//        beginConnection(componentUnderCursor.slot)
    }

    override fun onMouseDrag(event: MouseEvent) {
//        update(SwingUtils.screenToLocal(event.locationOnScreen, editor))
    }

    override fun onMouseUp(event: MouseEvent) {
//        val localPoint = SwingUtils.screenToLocal(event.locationOnScreen, editor)
//        val componentUnderCursor = SwingUtils.getComponentAt(editor, localPoint)
//
//        if (componentUnderCursor is SlotComponent) {
//            endConnection(componentUnderCursor.slot)
//        } else {
//            stop()
//        }
    }

//    override fun paintUnderNodes(g: Graphics) {
//        if (from!!.direction == INPUT) {
//            drawLink(g as Graphics2D, toLocation, fromLocation)
//        } else {
//            drawLink(g, fromLocation, toLocation)
//        }
//    }

    fun beginConnection(slot: Slot) {

        // unlink existing link from output (outputs can only link to one)
//        if (!slot.allowsMultipleLinks && editor.linkManager.isLinked(slot)) {
//            val link = editor.linkManager.findLink(slot)!!
//            editor.linkManager.unlink(link)
//            from = link.other(slot)
//        } else {
//            from = slot
//        }
//
//        fromLocation.location = editor.getSlotLocation(from!!)
//        toLocation.location = this.fromLocation
//
//        this.isConnecting = true
    }

    fun endConnection(to: Slot) {
        if (editor.linkManager.canLink(from!!, to)) {
            editor.linkManager.link(from!!, to)
        }
        stop()
    }

    fun update(mouseLocation: Point2D) {
        if (!isConnecting) {
            return
        }

//        val componentUnderCursor = getComponentAt(editor, mouseLocation)
//
//        // hover out
//        if (componentUnderCursor !is SlotComponent) {
//            hoveredComponent?.state = SlotComponent.State.NORMAL
//            hoveredComponent = null
//        }
//
//        if (componentUnderCursor is SlotComponent && componentUnderCursor !== hoveredComponent && componentUnderCursor.slot != from) {
//            hoveredComponent = componentUnderCursor
//            hoveredComponent?.state = if (editor.linkManager.canLink(from!!, componentUnderCursor.slot)) SlotComponent.State.ACCEPT else SlotComponent.State.DECLINE
//        }
//
//        toLocation.location = mouseLocation
    }

    fun stop() {
//        isConnecting = false
//        hoveredComponent?.state = SlotComponent.State.NORMAL
//        hoveredComponent = null
//        from = null
    }
}
