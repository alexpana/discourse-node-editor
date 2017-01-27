package com.manabreak.node_editor.ui.tools

import com.manabreak.node_editor.model.Slot
import com.manabreak.node_editor.ui.CubicCurveLink
import com.manabreak.node_editor.ui.NodeEditor
import com.manabreak.node_editor.ui.SlotComponent
import javafx.event.EventTarget
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent

class CreateLinkTool(editor: NodeEditor) : Tool(editor = editor) {

    var isConnecting = false

    var from: Slot? = null

    var hoveredComponent: SlotComponent? = null

    // Endpoints used for rendering the link
    var fromLocation = Point2D.ZERO
    var toLocation = Point2D.ZERO

    val interactiveLink = CubicCurveLink()

    override fun onMouseDown(event: MouseEvent) {
        val componentUnderCursor = event.target as SlotComponent

        beginConnection(componentUnderCursor.slot)
    }

    override fun onMouseDrag(event: MouseEvent) {
        update(event.target, event.x, event.y)
    }

    override fun onMouseUp(event: MouseEvent) {
        val componentUnderCursor = event.target

        if (componentUnderCursor is SlotComponent) {
            endConnection(componentUnderCursor.slot)
        } else {
            stop()
        }
    }

    fun beginConnection(slot: Slot) {
        // unlink existing link from output (outputs can only link to one)
        if (!slot.allowsMultipleLinks && editor.linkManager.isLinked(slot)) {
            val link = editor.linkManager.findLink(slot)!!
            editor.linkManager.unlink(link)
            from = link.other(slot)
        } else {
            from = slot
        }

        editor.backgroundPane.children.add(interactiveLink.backgroundCurve)
        editor.backgroundPane.children.add(interactiveLink.foregroundCurve)

        fromLocation = editor.getSlotLocation(from!!)
        toLocation = this.fromLocation

        interactiveLink.update(fromLocation, toLocation)

        this.isConnecting = true
    }

    fun endConnection(to: Slot) {
        if (editor.linkManager.canLink(from!!, to)) {
            editor.linkManager.link(from!!, to)
        }
        stop()
    }

    fun update(target: EventTarget?, x: Double, y: Double) {
        // hover out
        if (target !is SlotComponent) {
            hoveredComponent?.state = SlotComponent.State.NORMAL
            hoveredComponent = null
        }

        val slot = editor.getSlotUnderCursor(x, y)

        if (slot != null && target !== hoveredComponent && slot.slot != from) {
            hoveredComponent = slot
            hoveredComponent?.state = if (editor.linkManager.canLink(from!!, slot.slot)) SlotComponent.State.ACCEPT else SlotComponent.State.DECLINE
        }

        toLocation = Point2D(x, y)
        interactiveLink.update(fromLocation, toLocation)
    }

    fun stop() {
        editor.backgroundPane.children.remove(interactiveLink.backgroundCurve)
        editor.backgroundPane.children.remove(interactiveLink.foregroundCurve)
        isConnecting = false
        hoveredComponent?.state = SlotComponent.State.NORMAL
        hoveredComponent = null
        from = null
    }
}
