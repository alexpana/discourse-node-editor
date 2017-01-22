package com.manabreak.node_editor.tools

import com.manabreak.node_editor.NodeEditor
import com.manabreak.node_editor.SlotComponent
import com.manabreak.node_editor.SwingUtils.getComponentAt
import com.manabreak.node_editor.model.Slot
import java.awt.Point

class CreateLinkTool(editor: NodeEditor) : Tool(editor = editor) {

    var isConnecting = false

    var from: Slot? = null

    var hoveredComponent: SlotComponent? = null

    // Endpoints used for rendering the link
    val fromLocation = Point()
    val toLocation = Point()

    fun beginConnection(slot: Slot) {

        // unlink existing link from output (outputs can only link to one)
        if (!slot.allowsMultipleLinks && editor.linkManager.isLinked(slot)) {
            val link = editor.linkManager.findLink(slot)!!
            editor.linkManager.unlink(link)
            from = link.other(slot)
        } else {
            from = slot
        }

        fromLocation.location = editor.getSlotLocation(from!!)
        toLocation.location = this.fromLocation

        this.isConnecting = true
    }

    fun endConnection(to: Slot) {
        if (editor.linkManager.canLink(from!!, to)) {
            editor.linkManager.link(from!!, to)
        }
        stop()
    }

    fun update(mouseLocation: Point) {
        if (!isConnecting) {
            return
        }

        val componentUnderCursor = getComponentAt(editor, mouseLocation)

        // hover out
        if (componentUnderCursor !is SlotComponent) {
            hoveredComponent?.state = SlotComponent.State.NORMAL
            hoveredComponent = null
        }

        if (componentUnderCursor is SlotComponent && componentUnderCursor !== hoveredComponent && componentUnderCursor.slot != from) {
            hoveredComponent = componentUnderCursor
            hoveredComponent?.state = if (editor.linkManager.canLink(from!!, componentUnderCursor.slot)) SlotComponent.State.ACCEPT else SlotComponent.State.DECLINE
        }

        toLocation.location = mouseLocation
    }

    fun stop() {
        isConnecting = false
        hoveredComponent?.state = SlotComponent.State.NORMAL
        hoveredComponent = null
        from = null
    }
}
